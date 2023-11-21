package com.project.animal.missing.repository;

import com.project.animal.missing.domain.MissingLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface MissingLikeRepository extends JpaRepository<MissingLike, Long> {
  @Query("select ml from MissingLike ml where ml.post.missingId = :postId and ml.member.id = :memberId")
  Optional<MissingLike> findLikeByPostIdAndMemberId(@Param("postId") Long postId, @Param("memberId") Long memberId);

  @Query("select count(mpl.missingLikeId) from MissingLike mpl where mpl.post.missingId = :postId and mpl.status = 1")
  int likedCountByPostId(@Param("postId") Long postId);
}