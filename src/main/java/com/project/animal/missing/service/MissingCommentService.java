package com.project.animal.missing.service;

import com.project.animal.missing.domain.MissingComment;
import com.project.animal.missing.domain.MissingPost;
import com.project.animal.missing.dto.comment.MissingCommentNewDto;
import com.project.animal.missing.exception.CommentSaveFailException;
import com.project.animal.missing.exception.DetailNotFoundException;
import com.project.animal.missing.repository.MissingCommentRepository;
import com.project.animal.missing.repository.MissingPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MissingCommentService {

  private final MissingCommentRepository missingCommentRepository;
  private final MissingPostRepository missingPostRepository;

  public boolean createComment(MissingCommentNewDto dto) {
    try {
      Optional<MissingPost> maybePost =  missingPostRepository.findById(dto.getMissingId());
      MissingPost post = maybePost.orElseThrow(() -> new DetailNotFoundException(dto.getMissingId()));

      MissingComment comment = new MissingComment(dto.getMemberId(), post, dto.getComment(), dto.getParentId());
      MissingComment result = missingCommentRepository.save(comment);
      if (result == null) throw new Exception("no save result");
      return true;
    } catch (Exception ex) {
      log.error("Error in createComment: >> " + dto.toString());
      throw new CommentSaveFailException(ex.getMessage(), ex.getCause(), dto);
    }
  }
}
