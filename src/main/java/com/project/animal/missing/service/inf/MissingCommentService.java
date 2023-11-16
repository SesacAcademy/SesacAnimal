package com.project.animal.missing.service.inf;

import com.project.animal.missing.dto.comment.MissingCommentDeleteDto;
import com.project.animal.missing.dto.comment.MissingCommentEditDto;
import com.project.animal.missing.dto.comment.MissingCommentNewDto;

public interface MissingCommentService {
  boolean createComment(long memberId, MissingCommentNewDto dto);

  boolean editComment(MissingCommentEditDto dto);

  boolean deleteComment(MissingCommentDeleteDto dto);
}
