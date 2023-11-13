package com.project.animal.adoption.repository;

import com.project.animal.adoption.domain.AdoptionComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptionCommentRepository extends JpaRepository<AdoptionComment, Long> {


}
