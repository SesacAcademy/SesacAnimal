package com.project.animal.missing.service;

import com.project.animal.missing.domain.MissingComment;
import com.project.animal.missing.dto.MissingCommentNewDto;
import com.project.animal.missing.exception.CommentSaveFailException;
import com.project.animal.missing.repository.MissingCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MissingCommentService {

  private final MissingCommentRepository missingCommentRepository;


  public boolean createComment(MissingCommentNewDto dto) {
    try {
      MissingComment comment = new MissingComment(dto.getMissingId(), dto.getMemberId(), dto.getComment());
      MissingComment result = missingCommentRepository.save(comment);
      if (result == null) throw new Exception("no save result");
      return true;
    } catch (Exception ex) {
      log.error("Error in createComment: >> " + dto.toString());
      throw new CommentSaveFailException(ex.getMessage(), ex.getCause(), dto);
    }


  }
}
