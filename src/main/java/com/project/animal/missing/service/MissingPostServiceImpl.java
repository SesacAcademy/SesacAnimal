package com.project.animal.missing.service;

import com.project.animal.global.common.annotation.Profiling;
import com.project.animal.member.domain.Member;
import com.project.animal.member.repository.MemberRepository;
import com.project.animal.missing.domain.MissingComment;
import com.project.animal.missing.domain.MissingPost;
import com.project.animal.missing.domain.MissingPostImage;
import com.project.animal.missing.dto.*;
import com.project.animal.missing.dto.comment.MissingCommentListEntryDto;
import com.project.animal.missing.dto.image.MissingPostImageDto;
import com.project.animal.missing.exception.DetailNotFoundException;
import com.project.animal.missing.exception.PostDeleteFailException;
import com.project.animal.missing.exception.PostEditFailException;
import com.project.animal.missing.exception.PostSaveFailException;
import com.project.animal.missing.repository.MissingPostRepository;
import com.project.animal.missing.service.converter.DtoEntityConverter;
import com.project.animal.missing.service.inf.MissingPostImageService;
import com.project.animal.missing.service.inf.MissingPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Profiling
public class MissingPostServiceImpl implements MissingPostService {

  private final MemberRepository memberRepository;
  private final MissingPostRepository missingPostRepository;

  private final MissingPostImageService missingPostImageService;

  private final DtoEntityConverter converter;


  public ListResponseDto<MissingListEntryDto> getPostList(MissingFilterDto filter, Pageable pageable) {
    Page<MissingPost> pages = missingPostRepository.findByFilter(filter, pageable);

    int count = (int) pages.getTotalElements();
    List<MissingListEntryDto> posts = pages.stream()
            .map(this ::convertToMissingListEntryDto)
            .collect(Collectors.toList());

    return new ListResponseDto<>(count, posts);
  }

  private MissingListEntryDto convertToMissingListEntryDto(MissingPost entity) {
    MissingListEntryDto entry = MissingListEntryDto.fromMissingPost(entity);
    List<MissingPostImageDto> images = entity.getImages().stream()
            .map(image -> new MissingPostImageDto(image.getImageId(), image.getPath()))
            .collect(Collectors.toList());
    entry.addImages(images);
    return entry;
  }

  /*
  *  Question
  *  findById 로 가져올 때 양방향 매핑이 되어있으니까 images에 대한 레퍼런스도 가지고있다.
  *  이후에 findById 로 가져온 post에서 images에 접근하면, Images중에 외래키로 Postid를 가진애를
  *  다시 쿼리해서 가져오는건가요?
  * */
  public MissingDetailDto getPostDetail(long postId) {
    Optional<MissingPost> maybePost =  missingPostRepository.findById(postId);
    MissingPost post = maybePost.orElseThrow(() -> new DetailNotFoundException(postId));

    List<MissingCommentListEntryDto> comments =  createCommentList(post.getMissingId(), post.getComments());
    List<MissingPostImageDto> images = createImageList(post.getImages());

    MissingDetailDto detailDto = MissingDetailDto.fromMissingPost(post, comments, images);

    return detailDto;
  }

  private List<MissingPostImageDto> createImageList(List<MissingPostImage> images) {
    return images.stream()
            .filter((image) -> image.getIsActive() == 1)
            .map(image -> new MissingPostImageDto(image.getImageId(), image.getPath()))
            .collect(Collectors.toList());
  }

  private  List<MissingCommentListEntryDto> createCommentList(long postId, List<MissingComment> comments) {
    List<MissingCommentListEntryDto> wholeComments = comments.stream()
            .map((entity) -> MissingCommentListEntryDto.fromMissingComment(postId, entity))
            .collect(Collectors.toList());

    List<MissingCommentListEntryDto> parents = wholeComments.stream()
            .filter((comment) -> comment.getParentId() == null)
            .collect(Collectors.toList());

    Map<Long, List<MissingCommentListEntryDto>> groupByParentId = wholeComments.stream()
            .filter((comment) -> comment.getParentId() != null)
            .collect(Collectors.groupingBy(MissingCommentListEntryDto :: getParentId));

    List<MissingCommentListEntryDto> commentList = parents.stream()
            .map((comment) -> {
              List<MissingCommentListEntryDto> children = groupByParentId.getOrDefault(comment.getCommentId(), null);
              comment.setComments(children);
              return comment;
            }).collect(Collectors.toList());

    return commentList;

  }

  @Transactional(propagation = Propagation.REQUIRED)
  public boolean createPost(long memberId, MissingNewDto dto) {
    try {

      Optional<Member> maybeMember = memberRepository.findById(memberId);
      Member member = maybeMember.orElseThrow(() -> new RuntimeException("일치하는 회원이 존재하지 않습니다."));

      MissingPost post = converter.toMissingPost(member, dto);
      MissingPost result = missingPostRepository.save(post);

      missingPostImageService.createImage(dto.getImages(), post);
      if (result == null) throw new Exception("no save result");
      return true;

    } catch (Exception ex) {
      log.error("Error in createPost: >> " + dto.toString());
      throw new PostSaveFailException(ex.getMessage(), ex.getCause(), dto);
    }
  }

  public boolean deletePost(long postId) {
    Optional<MissingPost> maybePost = missingPostRepository.findById(postId);
    MissingPost postDetail = maybePost.orElseThrow(() -> new DetailNotFoundException(postId));

    try {
      postDetail.inactivatePost();
      MissingPost result = missingPostRepository.save(postDetail);
      if (result == null) throw new Exception("no save result");
      return true;
    } catch (Exception ex) {
      log.error("Error in deletePost: >> " + postId);
      throw new PostDeleteFailException(ex.getMessage(), ex.getCause(), postId);
    }
  }

  // TODO: find로 post 가져온 후에 값 변경후 dirty check로 commit 가능

  @Transactional
  public boolean editPost(long memberId, MissingEditDto dto) {

    try {
      Optional<Member> maybeMember = memberRepository.findById(memberId);
      Member member = maybeMember.orElseThrow(() -> new RuntimeException("일치하는 회원이 존재하지 않습니다."));

      MissingPost post = converter.toMissingPost(member, dto);
      MissingPost result = missingPostRepository.save(post);
      missingPostImageService.editImages(dto.getImages(), post, dto.getDeletedIds());

      if (result == null) throw new Exception("no edit result");
      return true;

    } catch (Exception ex) {
      log.error("Error in editPost: >> " + dto.toString());
      throw new PostEditFailException(ex.getMessage(), ex.getCause(), dto);
    }
  }
}