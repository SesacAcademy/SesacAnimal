package com.project.animal.member.domain;

import com.project.animal.global.common.constant.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * [참고] UserDetail
 * - https://programmer93.tistory.com/68
 */

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 100)
    private String email;

    @Column(length = 1000)
    private String password;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String phone;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(length = 20)
    private String type;

    @Column(name = "is_active" , length = 1)
    private Integer isActive;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime lastLoginAt;

    // 계정의 고유한 값을 리턴 --> ex. DB PK값, 중복이 없는 이메일, 아이디 값
    @Override
    public String getUsername() {
        return email;
    }

    // 계정의 비밀번호를 리턴
    @Override
    public String getPassword() {
        return password;
    }

    // 계정의 권한 목록을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        return authorities;
    }

    // 계정의 만료 여부 리턴 --> true (만료 안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정의 잠김 여부 리턴 --> true (잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호 만료 여부 리턴 --> true (만료 안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정의 활성화 여부 리턴 --> true (활성화 됨)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
