package com.project.animal.member.repository;

import com.project.animal.member.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select count(b) from Board b where b.member.id = :memberId and b.isActive = 1")
    Long getMyBoardCount(@Param("memberId") Long memberId);

    @Query("select b from Board b where b.member.id = :memberId and b.isActive = 1")
    Page<Board> getMyBoardList(@Param("memberId") Long memberId, Pageable pageable);
}
