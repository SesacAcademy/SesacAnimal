package com.project.animal.missing.repository;

import com.project.animal.missing.domain.MissingComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MissingCommentRepository  extends JpaRepository<MissingComment, Long> {
  @Modifying
  @Query("delete from MissingComment mc where mc.parentComment.comment_id = :commentId")
  void deleteByParentCommentId(@Param("commentId") Long commentId);
}
