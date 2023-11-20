package com.project.animal.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class ChangePasswordFormDto {

    @Pattern(regexp = "^[a-zA-Z0-9]{10,}$", message = "영어 대소문자와 숫자로 구성된 10자 이상의 문자열을 입력하세요.")
    private String oldPassword;

    @Pattern(regexp = "^[a-zA-Z0-9]{10,}$", message = "영어 대소문자와 숫자로 구성된 10자 이상의 문자열을 입력하세요.")
    private String newPassword;
}
