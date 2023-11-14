package com.project.animal.member.controller;

import com.project.animal.global.common.constant.EndPoint;
import com.project.animal.global.common.constant.ViewName;
import com.project.animal.global.common.dto.ResponseDto;
import com.project.animal.global.common.provider.MailAuthCodeProvider;
import com.project.animal.member.domain.Member;
import com.project.animal.member.dto.CheckMailTokenDto;
import com.project.animal.member.dto.FindMemberEmailFormDto;
import com.project.animal.member.dto.SignupFormDto;
import com.project.animal.member.exception.InvalidCodeException;
import com.project.animal.member.exception.NestedEmailException;
import com.project.animal.member.exception.NestedNicknameException;
import com.project.animal.member.exception.NotFoundException;
import com.project.animal.member.service.inf.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import static com.project.animal.global.common.constant.EndPoint.FIND_EMAIL_API;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final MailAuthCodeProvider mailTokenProvider;

    /**
     * 회원가입 페이지로 이동하는 Controller
     * @version 0.1
     * @author 박성수
     * @return String (회원가입 페이지 뷰 이름)
     */
    @GetMapping(EndPoint.SIGNUP)
    public String signupForm() {
        // 회원가입 폼으로 이동
        return ViewName.SIGNUP_VIEW;
    }

    /**
     * 회원 가입을 담당하는 Controller로 회원가입 폼에서 입력한 데이터가 유효한 지 검증합니다.
     * @version 0.1
     * @author 박성수
     * @param signupFormDto (SignupFormDto 객체)
     * @return ResponseDto<String> (API 응답 DTO)
     * @throws NestedEmailException (이메일이 중복된 경우, 해당 예외 발생)
     * @throws NestedNicknameException (닉네임이 중복된 경우, 해당 예외 발생)
     * @throws InvalidCodeException (이메일 인증 번호가 유효하지 않은 경우, 해당 예외 발생)
     */
    @ResponseBody
    @PostMapping(EndPoint.SIGNUP_API)
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<String> signup(@RequestBody @Validated SignupFormDto signupFormDto) {
        // 회원가입 진행
        memberService.save(signupFormDto);

        log.info("{} 이메일로 회원 가입 되었습니다.", signupFormDto.getEmail());

        return new ResponseDto<>(HttpStatus.OK.value(), "null", "회원가입 완료");
    }

    /**
     * 이메일 인증 번호 발급을 담당하는 Controller 입니다.
     * @version 0.1
     * @author 박성수
     * @param email (이메일)
     * @return ResponseDto<String> (API 응답 DTO)
     * @throws NestedEmailException (이메일이 중복된 경우, 해당 예외 발생)
     * @throws MailSendException (메일 발송에 실패할 시, 예외 발생)
     */
    @ResponseBody
    @GetMapping(EndPoint.EMAIL_API)
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<String> getMailToken(@RequestParam(required = true, defaultValue = "None") String email) {
        // 이메일 인증 번호 발급
        memberService.createMailToken(email);

        log.info("{} 이메일로 인증 번호를 발급하였습니다. (이메일)", email);

        return new ResponseDto<>(HttpStatus.NO_CONTENT.value(), "null", "인증번호 발급 Ok");
    }

    /**
     * 이메일 인증 번호 확인을 담당하는 Controller로 이메일 인증 폼에서 입력받은 데이터가 형식에 맞는지 검증합니다.
     * @version 0.1
     * @author 박성수
     * @param checkMailTokenDto (CheckMailTokenDto 객체)
     * @return ResponseDto<String> (API 응답 DTO)
     * @throws InvalidCodeException (이메일 인증 번호가 유효하지 않은 경우, 해당 예외 발생)
     */
    @ResponseBody
    @PostMapping(EndPoint.EMAIL_API)
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<String> checkMailToken(@RequestBody @Validated CheckMailTokenDto checkMailTokenDto) {
        // 이메일 인증번호 인증
        memberService.checkMailToken(checkMailTokenDto.getEmail(), checkMailTokenDto.getToken());

        log.info("{} 이메일로 발급된 인증번호가 확인되었습니다.", checkMailTokenDto.getEmail());

        return new ResponseDto<>(HttpStatus.NO_CONTENT.value(), "null", "인증번호가 확인되었습니다.");
    }

    /**
     * 아이디 찾기를 처리하는 Controller로 아이디 찾기 폼에서 입력한 데이터가 형식에 맞는지 검증합니다.
     * @version 0.1
     * @author 박성수
     * @param findMemberEmailFormDto (FindMemberEmailDto 객체)
     * @return ResponseDto<String> (API 응답 DTO)
     * @throws NotFoundException (해당 정보로 가입된 아이디가 없는 경우, 해당 예외 발생)
     */
    @ResponseBody
    @PostMapping(FIND_EMAIL_API)
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<String> findMemberId(@RequestBody @Validated FindMemberEmailFormDto findMemberEmailFormDto) {
        // 아이디 찾기
        Member findMember = memberService.findEmail(findMemberEmailFormDto);

        log.info("이름 : {}, 휴대폰 번호 : {}에 해당하는 아이디 찾기를 수행하였습니다.", findMember.getName(), findMember.getPhone());

        return new ResponseDto<>(HttpStatus.OK.value(), findMember.getEmail(), "아이디 찾기 Ok");
    }
}