package com.project.animal.member.service;

import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.member.domain.Member;
import com.project.animal.member.dto.ChangePasswordFormDto;
import com.project.animal.member.exception.LoginException;
import com.project.animal.member.exception.NotFoundException;
import com.project.animal.member.exception.WrongPasswordException;
import com.project.animal.member.repository.MemberRepository;
import com.project.animal.member.service.inf.LoginService;
import com.project.animal.member.service.inf.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageServiceImp implements MyPageService {

    private final MemberRepository memberRepository;

    private final LoginService loginService;

    private final PasswordEncoder encoder;

    /**
     * 마이 페이지에서 필요한 사용자 정보 데이터를 가져오는 Service이다.
     *
     * @version 0.1
     * @author 박성수
     * @param member MemberDto 객체
     * @return Member 객체
     */
    @Override
    public Member getMember(MemberDto member) {
        return memberRepository.findById(member.getId()).orElseThrow(NotFoundException::new);
    }

    /**
     * 회원 탈퇴를 담당하는 Service로 DB에 저장된 사용자의 상태 컬럼을 0으로 (삭제 대기) 변경하고 기존 쿠키에 저장된 JWT 토큰을
     * 삭제한다.
     *
     * @version 0.1
     * @author 박성수
     * @param member MemberDto 객체
     * @param response HttpServletResponse 객체
     */
    @Override
    @Transactional
    public void deleteMember(MemberDto member, HttpServletResponse response) {
        // Member 객체 조회
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(NotFoundException::new);

        // Member Status 변경 (삭제 대기)
        findMember.setIsActive(0);
        
        // 쿠키 삭제
        loginService.logout(member, response);
    }

    /**
     * 비밀번호 변경을 담당하는 Service로 사용자가 입력한 비밀번호가 기존 DB에 저장된 비밀번호와 일치하는지 비교하고 일치하면
     * 새로운 비밀번호로 변경한다.
     *
     * @version 0.1
     * @author 박성수
     * @param changePasswordFormDto ChangePasswordFormDto 객체
     * @param member MemberDto 객체
     * @throws WrongPasswordException 기존 비밀번호를 잘못 입력한 경우, 해당 예외 발생
     */
    @Override
    @Transactional
    public void changePassword(ChangePasswordFormDto changePasswordFormDto, MemberDto member) {
        // Member 객체 조회
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(NotFoundException::new);

        // 비밀번호 비교
        boolean matchPassword = encoder.matches(changePasswordFormDto.getOldPassword(), findMember.getPassword());

        // 비밀번호 잘못 입력했을 경우, 예외 발생
        if (!matchPassword) {
            throw new WrongPasswordException("아이디(" + member.getEmail() + ") 비밀번호 변경 실패");
        }

        // 비밀번호 변경
        findMember.setPassword(encoder.encode(changePasswordFormDto.getNewPassword()));
    }
}
