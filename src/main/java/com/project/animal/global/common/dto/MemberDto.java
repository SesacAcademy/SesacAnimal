package com.project.animal.global.common.dto;

import com.project.animal.global.common.constant.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDto {

    private Long id;

    private String email;

    private Role role;
}
