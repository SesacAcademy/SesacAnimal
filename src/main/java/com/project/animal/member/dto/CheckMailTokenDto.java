package com.project.animal.member.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CheckMailTokenDto {

    @Email
    private String email;

    @Pattern(regexp = "^\\d{6}$", message = "인증번호 형식이 틀렸습니다.")
    private String token;
}
