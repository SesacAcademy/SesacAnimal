package com.project.animal.missing.repository;

import com.project.animal.missing.domain.MissingComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissingCommentRepository  extends JpaRepository<MissingComment, Long> {
  void deleteByParentId(Long parentId);
}
