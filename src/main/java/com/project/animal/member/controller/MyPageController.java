package com.project.animal.member.controller;

import com.project.animal.global.common.annotation.Member;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.global.common.dto.ResponseDto;
import com.project.animal.member.dto.ChangePasswordFormDto;
import com.project.animal.member.service.inf.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static com.project.animal.global.common.constant.ViewName.MYPAGE_VIEW;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @ModelAttribute("member")
    public MemberDto addMemberInModel(@Member MemberDto member) {
        return member;
    }

    /**
     * 마이 페이지로 이동하는 Controller이다.
     *
     * @version 0.1
     * @author 박성수
     * @return String (마이 페이지 뷰 이름)
     */
    @GetMapping("/v1/member/mypage/info")
    public String myPage(@Member MemberDto member, Model model) {
        // 회원 정보 페이지로 이동
        model.addAttribute("memberInfo", myPageService.getMember(member));

        return MYPAGE_VIEW;
    }

    /**
     *
     * @return
     */
    @ResponseBody
    @DeleteMapping("/v1/api/member")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<String> deleteMember(@Member MemberDto memberDto, HttpServletResponse response) {
        // 회원 탈퇴
        myPageService.deleteMember(memberDto, response);

        log.info("{} 계정이 회원 탈퇴 되었습니다.", memberDto.getEmail());

        return new ResponseDto<>(HttpStatus.OK.value(), "null", "회원 탈퇴 완료");
    }


    @GetMapping("/v1/member/mypage/password")
    public String changePasswordForm() {
        // 비밀번호 변경 폼으로 이동
        return "member/mypage_password";
    }

    @ResponseBody
    @PatchMapping("/v1/api/member/mypage/password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<String> changePassword(@RequestBody @Validated ChangePasswordFormDto changePasswordFormDto,
                                 @Member MemberDto memberDto) {
        // 비밀번호 변경
        myPageService.changePassword(changePasswordFormDto, memberDto);

        log.info("{} 계정의 비밀번호를 변경하였습니다.", memberDto.getEmail());

        return new ResponseDto<>(HttpStatus.OK.value(), "null", "비밀번호 변경 완료");
    }

    @GetMapping("/v1/member/mypage/wishList")
    public String wishList() {
        return "member/mypage_wishList";
    }

    @GetMapping("/v1/member/mypage/boardList")
    public String myBoardList() {
        return "member/mypage_boardList";
    }

    @GetMapping("/v1/member/mypage/adoptionList")
    public String adoptionList() {
        return "member/mypage_adoptionList";
    }
}
