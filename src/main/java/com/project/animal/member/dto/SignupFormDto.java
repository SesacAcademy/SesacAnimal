package com.project.animal.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class SignupFormDto {
    @Email(message = "이메일 형식이 틀렸습니다.")
    private String email;

    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{3,12}", message = "닉네임을 형식에 맞게 입력해주세요.")
    private String nickname;

    @Pattern(regexp = "^\\d{6}$", message = "인증 번호 형식이 틀렸습니다.")
    private String authCode;

    @Pattern(regexp = "^[가-힣]{2,4}$", message = "이름을 제대로 입력해주세요.")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9]{10,}$", message = "영어 대소문자와 숫자로 구성된 10자 이상의 문자열을 입력하세요.")
    private String password;

    @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "휴대폰 번호 형식이 틀렸습니다.")
    private String phone;
}
