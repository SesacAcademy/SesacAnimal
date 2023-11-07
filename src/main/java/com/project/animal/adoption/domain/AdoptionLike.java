package com.project.animal.adoption.domain;

import com.project.animal.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "adoption_like")
public class AdoptionLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adoption_like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "adoption_id")
    private Adoption adoption;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    private int status;

    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updatedAt;
}
