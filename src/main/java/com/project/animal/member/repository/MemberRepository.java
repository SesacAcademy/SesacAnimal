package com.project.animal.member.repository;

import com.project.animal.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndType(String email, String type);
    Optional<Member> findByEmailAndTypeAndIsActive(String email, String type, Integer isActive);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByNameAndPhone(String name, String phone);
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<Member> findByEmailAndNameAndPhone(String email, String name, String phone);
}
