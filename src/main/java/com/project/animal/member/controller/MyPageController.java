package com.project.animal.member.controller;

import com.project.animal.global.common.annotation.Member;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.member.service.inf.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

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
    public String mypage(@Member MemberDto member, Model model) {
        model.addAttribute("memberInfo", myPageService.getMember(member));

        return MYPAGE_VIEW;
    }

    @GetMapping("/v1/member/mypage/password")
    public String changePassword() {

        return "member/mypage_password";
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
