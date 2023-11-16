package com.project.animal.member.controller;

import com.project.animal.global.common.annotation.Member;
import com.project.animal.global.common.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import static com.project.animal.global.common.constant.ViewName.MYPAGE_VIEW;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MyPageController {

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
    @GetMapping("/v1/member/mypage")
    public String mypage() {
        return MYPAGE_VIEW;
    }
}
