package com.project.animal.member.service;

import com.project.animal.global.common.constant.Role;
import com.project.animal.global.common.provider.MailAuthCodeProvider;
import com.project.animal.global.common.provider.SmsAuthCodeProvider;
import com.project.animal.member.domain.Member;
import com.project.animal.member.dto.FindMemberEmailFormDto;
import com.project.animal.member.dto.FindMemberPwdFormDto;
import com.project.animal.member.dto.SignupFormDto;
import com.project.animal.member.exception.InvalidCodeException;
import com.project.animal.member.exception.NestedEmailException;
import com.project.animal.member.exception.NestedNicknameException;
import com.project.animal.member.exception.NotFoundException;
import com.project.animal.member.repository.MemberRepository;
import com.project.animal.member.service.inf.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static com.project.animal.global.common.constant.AuthType.JWT;
import static com.project.animal.global.common.constant.AuthType.MAIL;

@Service
@RequiredArgsConstructor
public class MemberServiceImp implements MemberService {

    private final MemberRepository memberRepository;

    private final MailAuthCodeProvider mailAuthCodeProvider;

    private final SmsAuthCodeProvider smsAuthCodeProvider;

    private final PasswordEncoder encoder;

    /**
     * 회원가입을 처리하는 Service로 Controller에서 전달 받은 SignupFormDto 객체를 이용하여 이메일, 닉네임이 중복되는지 확인하고
     * 최종적으로 이메일 인증번호를 체크한 다음 DB에 회원 정보를 저장한다.
     *
     * @version 0.1
     * @author 박성수
     * @param signupFormDto SignupFormDto 객체
     * @throws NestedEmailException 이메일이 중복된 경우, 해당 예외 발생
     * @throws NestedNicknameException 닉네임이 중복된 경우, 해당 예외 발생
     * @throws InvalidCodeException 이메일 인증 번호가 유효하지 않은 경우, 해당 예외 발생
     */
    @Override
    @Transactional
    public void save(SignupFormDto signupFormDto) {
        // 이메일 중복 여부 체크
        checkNestedEmail(signupFormDto.getEmail());

        // 닉네임 중복 여부 체크
        checkNestedNickname(signupFormDto.getNickname());
        
        // 이메일 인증 번호 체크
        checkMailAuthCode(signupFormDto.getEmail(), signupFormDto.getAuthCode());

        LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));

        Member member = Member.builder()
                .email(signupFormDto.getEmail())
                .nickname(signupFormDto.getNickname())
                .name(signupFormDto.getName())
                .password(encoder.encode(signupFormDto.getPassword()))
                .phone(signupFormDto.getPhone())
                .type(MAIL.name())
                .role(Role.ROLE_USER)
                .isActive(1)
                .lastLoginAt(dateTime)
                .build();

        memberRepository.save(member);
    }

    /**
     * 이메일 인증 번호 발급을 담당하는 Service이다.
     *
     * @version 0.1
     * @author 박성수
     * @param email 이메일
     * @throws NestedEmailException 이메일이 중복된 경우, 해당 예외 발생
     * @throws MailSendException 메일 발송에 실패할 시, 예외 발생
     */
    @Override
    @Transactional
    public void createMailAuthCode(String email) {
        // 이메일 중복 여부 체크
        checkNestedEmail(email);

        // 이메일 인증 번호 발급
        mailAuthCodeProvider.generateAuthCode(email);
    }

    /**
     * 이메일 인증 번호 확인을 담당하는 Service로 Controller에서 전달 받은 인증 번호가 Redis 서버에 저장된 인증 번호와 일치하는지
     * 비교합니다.
     *
     * @version 0.1
     * @author 박성수
     * @param email 유저 이메일
     * @param authCode 이메일 인증 번호
     * @throws InvalidCodeException 이메일 인증 번호가 유효하지 않은 경우, 해당 예외 발생
     */
    @Override
    public void checkMailAuthCode(String email, String authCode) {
        // 이메일 인증 번호 체크
        if(!mailAuthCodeProvider.validateAuthCode(email, authCode)) {
            throw new InvalidCodeException("[이메일] 유효하지 않은 인증 번호입니다.");
        }
    }

    @Override
    public void createSmsAuthCode(FindMemberPwdFormDto memberPwdFormDto) {
        Optional<Member> findMember = memberRepository.findByEmailAndNameAndPhone(memberPwdFormDto.getEmail(),
                                                    memberPwdFormDto.getName(), memberPwdFormDto.getPhone());

        Member member = findMember.orElseThrow(() -> {
            throw new NotFoundException("입력하신 정보가 틀렸습니다.");
        });

        // 문자 인증 번호 발급
        smsAuthCodeProvider.generateAuthCode(member.getPhone());
    }

    /**
     * 아이디 찾기를 담당하는 Service로 Controller에서 전달받은 FindMemberEmailFormDto 객체를 이용하여 DB에 해당하는 정보가
     * 있는지 확인합니다.
     *
     * @version 0.1
     * @author 박성수
     * @param memberEmailFormDto MemberEmailFormDto 객체
     * @return Member 객체
     * @throws NotFoundException 해당 정보로 가입된 아이디가 없는 경우, 해당 예외 발생
     */
    @Override
    public Member findEmail(FindMemberEmailFormDto memberEmailFormDto) {
        Optional<Member> findMember = memberRepository.findByNameAndPhone(memberEmailFormDto.getName(), memberEmailFormDto.getPhone());

        return findMember.orElseThrow(() -> new NotFoundException("해당 정보로 가입된 아이디가 없습니다."));
    }

    /**
     * 중복된 이메일인지 확인하는 메소드입니다.
     *
     * @version 0.1
     * @author 박성수
     * @param email 유저 이메일
     * @throws NestedEmailException 중복 이메일인 경우, 해당 예외 발생
     */
    private void checkNestedEmail(String email) {
        memberRepository.findByEmailAndType(email, MAIL.name())
                .ifPresent((x) -> {
                    throw new NestedEmailException("중복된 이메일입니다.");
                });
    }

    /**
     * 중복된 닉네임인지 확인하는 메소드입니다.
     * @version 0.1
     * @author 박성수
     * @param nickname 유저 닉네임
     * @throws NestedNicknameException 중복 닉네임인 경우, 해당 예외 발생
     */
    private void checkNestedNickname(String nickname) {
        memberRepository.findByNickname(nickname)
                .ifPresent((x) -> {
                    throw new NestedNicknameException("중복된 닉네임입니다.");
                });
    }

}