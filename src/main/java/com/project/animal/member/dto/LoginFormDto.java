package com.project.animal.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class LoginFormDto {
    @Email(message = "이메일 형식이 틀렸습니다.")
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9]{10,}$", message = "영어 대소문자와 숫자로 구성된 10자 이상의 문자열을 입력하세요.")
    private String password;
}
