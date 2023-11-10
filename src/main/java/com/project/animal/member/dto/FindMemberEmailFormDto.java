package com.project.animal.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class FindMemberEmailFormDto {

    @Pattern(regexp = "^[가-힣]{2,4}$", message = "이름을 입력해주세요.")
    private String name;

    @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "휴대폰 번호 형식이 틀렸습니다.")
    private String phone;
}
