package com.project.animal.member.service;

import com.project.animal.global.common.constant.Role;
import com.project.animal.global.common.dto.MailDto;
import com.project.animal.global.common.provider.MailAuthCodeProvider;
import com.project.animal.global.common.provider.MailServiceProvider;
import com.project.animal.global.common.provider.SmsAuthCodeProvider;
import com.project.animal.member.domain.Member;
import com.project.animal.member.dto.CheckSmsAuthCodeDto;
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
import java.util.Random;

import static com.project.animal.global.common.constant.AuthType.JWT;
import static com.project.animal.global.common.constant.AuthType.MAIL;

@Service
@RequiredArgsConstructor
public class MemberServiceImp implements MemberService {

    private final MemberRepository memberRepository;

    private final MailServiceProvider mailServiceProvider;

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

        LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

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

    /**
     * 문자 인증 번호 발급을 담당하는 Service이다.
     *
     * @version 0.1
     * @author 박성수
     * @param memberPwdFormDto FindMemberPwdFormDto 객체
     * @throws NotFoundException 해당 정보로 가입된 아이디가 없는 경우, 해당 예외 발생
     */
    @Override
    @Transactional
    public void createSmsAuthCode(FindMemberPwdFormDto memberPwdFormDto) {
        // 사용자 정보 조회
        Optional<Member> findMember = memberRepository.findByEmailAndNameAndPhone(memberPwdFormDto.getEmail(),
                                                    memberPwdFormDto.getName(), memberPwdFormDto.getPhone());

        Member member = findMember.orElseThrow(() -> new NotFoundException("입력하신 정보가 틀렸습니다."));

        // 문자 인증 번호 발급
        smsAuthCodeProvider.generateAuthCode(memberPwdFormDto.getPhone());
    }

    /**
     * 문자 인증 번호 확인을 담당하는 Service로 Controller에서 전달 받은 인증 번호가 Redis 서버에 저장된 인증 번호와 일치하는지
     * 비교하고 일치한다면 새로운 비밀번호를 생성하여 유저 메일로 보내준 뒤, DB에 있는 패스워드를 변경합니다.
     *
     * @version 0.1
     * @author 박성수
     * @param smsAuthCodeDto CheckSmsAuthCodeDto 객체
     * @throws NotFoundException 해당 정보로 가입된 아이디가 없는 경우, 해당 예외 발생
     * @throws InvalidCodeException 문자 인증 번호가 유효하지 않은 경우, 해당 예외 발생
     * @throws MailSendException 메일 발송에 실패할 시, 예외 발생
     */
    @Override
    @Transactional
    public void createTempPassword(CheckSmsAuthCodeDto smsAuthCodeDto) {
        // 사용자 정보 조회
        Optional<Member> findMember = memberRepository.findByEmailAndNameAndPhone(smsAuthCodeDto.getEmail(),
                smsAuthCodeDto.getName(), smsAuthCodeDto.getPhone());

        Member member = findMember.orElseThrow(() -> new NotFoundException("입력하신 정보가 틀렸습니다."));

        // 문자 인증 번호 체크
        if(!smsAuthCodeProvider.validateAuthCode(smsAuthCodeDto.getPhone(), smsAuthCodeDto.getAuthCode())) {
            throw new InvalidCodeException("[문자] 유효하지 않은 인증 번호입니다.");
        }

        // 임시 비밀번호 발급
        String tempPassword = createRandomString();

        // 메일 생성
        MailDto mail = MailDto.builder()
                              .title("[새싹 애니멀] 임시 비밀번호입니다.")
                              .content("임시 비밀번호 : " + tempPassword)
                              .build();
        
        // 메일 전송
        mailServiceProvider.sendMail(smsAuthCodeDto.getEmail(), mail);
        
        // DB에 저장된 사용자 비밀번호 수정
        member.setPassword(encoder.encode(tempPassword));
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
        // 사용자 정보 조회
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
     *
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

    /**
     * 랜덤 문자열을 생성하는 메소드입니다.
     *
     * @version 0.1
     * @author 박성수
     * @return String 객체 (랜덤 문자열)
     */
    private String createRandomString() {
        Random rd = new Random();
        StringBuilder tempPassword = new StringBuilder();

        for (int i=1; i<=15; i++) {
            // true인 경우, 영어 소문자
            if (rd.nextBoolean())
                tempPassword.append((char)(rd.nextInt(26) + 97));

            // false인 경우, 숫자
            else
                tempPassword.append(rd.nextInt(10));
        }
        return tempPassword.toString();
    }
}