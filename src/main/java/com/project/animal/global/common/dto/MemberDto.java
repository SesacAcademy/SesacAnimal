package com.project.animal.global.common.dto;

import com.project.animal.global.common.constant.Role;
import com.project.animal.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDto {

    private long id;

    private String email;

    private Role role;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.email = new String(member.getEmail());
        this.role = member.getRole();
    }
}
