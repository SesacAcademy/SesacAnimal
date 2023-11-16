package com.project.animal.member.repository;

import com.project.animal.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndType(String email, String type);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByNameAndPhone(String name, String phone);
    Optional<Member> findByEmailAndNameAndPhone(String email, String name, String phone);
}
