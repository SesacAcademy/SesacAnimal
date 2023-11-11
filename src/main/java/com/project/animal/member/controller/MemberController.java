package com.project.animal.member.controller;

import com.project.animal.global.common.constant.EndPoint;
import com.project.animal.global.common.constant.ViewName;
import com.project.animal.global.common.dto.ResponseDto;
import com.project.animal.global.common.provider.MailAuthCodeProvider;
import com.project.animal.member.domain.Member;
import com.project.animal.member.dto.CheckMailTokenDto;
import com.project.animal.member.dto.FindMemberEmailFormDto;
import com.project.animal.member.dto.SignupFormDto;
import com.project.animal.member.service.inf.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @GetMapping(EndPoint.SIGNUP)
    public String signupForm() {
        return ViewName.SIGNUP_VIEW;
    }

    @ResponseBody
    @PostMapping(EndPoint.SIGNUP_API)
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<String> signup(@RequestBody @Validated SignupFormDto signupFormDto) {
        memberService.save(signupFormDto);
        return new ResponseDto<>(HttpStatus.OK.value(), "null", "회원가입 완료");
    }

    @ResponseBody
    @GetMapping(EndPoint.EMAIL_API)
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<String> getMailToken(@RequestParam(required = true, defaultValue = "None") String email) {
        // 이메일 토큰 발급
        memberService.createMailToken(email);

        return new ResponseDto<>(HttpStatus.NO_CONTENT.value(), "null", "인증번호 발급 Ok");
    }

    @ResponseBody
    @PostMapping(EndPoint.EMAIL_API)
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<String> checkMailToken(@RequestBody CheckMailTokenDto checkMailTokenDto) {
        // 이메일 토큰 인증
        memberService.checkMailToken(checkMailTokenDto.getEmail(), checkMailTokenDto.getToken());

        return new ResponseDto<>(HttpStatus.NO_CONTENT.value(), "null", "인증번호가 확인되었습니다.");
    }

    @ResponseBody
    @PostMapping(FIND_EMAIL_API)
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<String> findMemberId(@RequestBody @Validated FindMemberEmailFormDto findMemberEmailFormDto) {
        // 아이디 찾기
        Member findMember = memberService.findEmail(findMemberEmailFormDto);

        return new ResponseDto<>(HttpStatus.OK.value(), findMember.getEmail(), "아이디 찾기 Ok");
    }
}