package com.project.animal.missing.service;

import com.project.animal.missing.domain.MissingComment;
import com.project.animal.missing.domain.MissingPost;
import com.project.animal.missing.dto.*;
import com.project.animal.missing.dto.comment.MissingCommentListEntryDto;
import com.project.animal.missing.exception.DetailNotFoundException;
import com.project.animal.missing.exception.PostDeleteFailException;
import com.project.animal.missing.exception.PostEditFailException;
import com.project.animal.missing.exception.PostSaveFailException;
import com.project.animal.missing.repository.MissingPostRepository;
import com.project.animal.missing.service.converter.DtoEntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MissingPostService {

  private final MissingPostRepository missingPostRepository;

  private final DtoEntityConverter converter;

  public ListResponseDto<MissingListEntryDto> getPostList(MissingFilterDto filter, Pageable pageable) {
    Page<MissingPost> pages = missingPostRepository.findByFilter(filter, pageable);

    int count = (int) pages.getTotalElements();
    List<MissingListEntryDto> posts = pages.stream()
            .map((entity) -> MissingListEntryDto.fromMissingPost(entity))
            .collect(Collectors.toList());

    return new ListResponseDto<>(count, posts);
  }

  public MissingDetailDto getPostDetail(long postId) {
    Optional<MissingPost> maybePost =  missingPostRepository.findById(postId);
    MissingPost post = maybePost.orElseThrow(() -> new DetailNotFoundException(postId));

    log.info("test: >> " + post.getComments().size());
    List<MissingCommentListEntryDto> comments =  createCommentList(post.getMissingId(), post.getComments());
    log.info("test2: >> " + comments.size());
    MissingDetailDto detailDto = MissingDetailDto.fromMissingPost(post, comments);

    return detailDto;
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

  public boolean createPost(MissingNewDto dto) {
    try {
      MissingPost post = converter.toMissingPost(dto);
      MissingPost result = missingPostRepository.save(post);

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
    log.info("dto:>> " + dto.toString());
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
