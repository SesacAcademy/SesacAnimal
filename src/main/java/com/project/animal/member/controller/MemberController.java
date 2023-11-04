package com.project.animal.member.controller;

import com.project.animal.global.common.dto.ResponseDto;
import com.project.animal.global.common.provider.MailTokenProvider;
import com.project.animal.member.dto.CheckMailTokenDto;
import com.project.animal.member.service.inf.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final MailTokenProvider mailTokenProvider;

    @GetMapping("/v1/auth/signup")
    public String signupForm() {
        return "/member/signup";
    }

    @ResponseBody
    @PostMapping("/v1/api/auth/signup")
    public ResponseEntity<String> signup() {
        return null;
    }

    @ResponseBody
    @GetMapping("/v1/api/auth/email")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<String> getMailToken(@RequestParam(required = true, defaultValue = "None") String email) {
        // 이메일 토큰 발급
        memberService.createMailToken(email);

        return new ResponseDto<>(HttpStatus.NO_CONTENT.value(), "null", "인증번호 발급 Ok");
    }

    @ResponseBody
    @PostMapping("/v1/api/auth/email")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<String> checkMailToken(@RequestBody CheckMailTokenDto checkMailTokenDto) {
        // 이메일 토큰 인증
        memberService.checkMailToken(checkMailTokenDto.getEmail(), checkMailTokenDto.getToken());

        return new ResponseDto<>(HttpStatus.NO_CONTENT.value(), "null", "인증번호가 확인되었습니다.");
    }
}