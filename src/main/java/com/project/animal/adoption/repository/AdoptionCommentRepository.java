package com.project.animal.adoption.repository;

import com.project.animal.adoption.domain.AdoptionComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdoptionCommentRepository extends JpaRepository<AdoptionComment, Long> {

    // 게시글에 따른 리스트를 불러오는 함수
    List<AdoptionComment> findByAdoptionId(Long adoptionId);

    List<AdoptionComment> findTopLevelCommentsByAdoptionId(Long adoptionId);


}
