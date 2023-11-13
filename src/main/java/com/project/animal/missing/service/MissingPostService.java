package com.project.animal.missing.service;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MissingPostService {

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
            .map(image -> new MissingPostImageDto(image.getImage_id(), image.getPath()))
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
            .map(image -> new MissingPostImageDto(image.getImage_id(), image.getPath()))
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

  @Transactional
  public boolean createPost(MissingNewDto dto) {
    try {
      MissingPost post = converter.toMissingPost(dto);
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

  public boolean editPost(MissingEditDto dto) {
    
    try {
      MissingPost post = converter.toMissingPost(dto);
      MissingPost result = missingPostRepository.save(post);

      if (result == null) throw new Exception("no edit result");
      return true;

    } catch (Exception ex) {
      log.error("Error in editPost: >> " + dto.toString());
      throw new PostEditFailException(ex.getMessage(), ex.getCause(), dto);
    }
  }
}
