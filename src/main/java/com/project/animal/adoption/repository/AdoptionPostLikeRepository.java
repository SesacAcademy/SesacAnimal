package com.project.animal.adoption.repository;

import com.project.animal.adoption.domain.Adoption;
import com.project.animal.adoption.domain.AdoptionComment;
import com.project.animal.adoption.domain.AdoptionPostLike;
import com.project.animal.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AdoptionPostLikeRepository extends JpaRepository<AdoptionPostLike, Long> {

    AdoptionPostLike findByAdoptionId(Long id);

//    AdoptionPostLike findByAdoptionAndMember(Long id, Member member);
    AdoptionPostLike findByAdoptionAndMember(Adoption adoption, Member member);

    // 추가된 메소드
    List<AdoptionPostLike> findByAdoptionIdAndStatus(Long adoptionId, int status);


}
