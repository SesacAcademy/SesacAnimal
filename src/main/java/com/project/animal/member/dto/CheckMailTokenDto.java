package com.project.animal.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckMailTokenDto {
    private String email;

    private String token;
}
