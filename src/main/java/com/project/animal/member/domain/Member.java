package com.project.animal.member.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

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

    @Column(length = 20)
    private String grade;

    @Column(length = 20)
    private String type;

    @Column(length = 1)
    private String status;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime lastLoginAt;
}
