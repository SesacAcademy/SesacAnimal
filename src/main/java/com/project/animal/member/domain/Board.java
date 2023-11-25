package com.project.animal.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Immutable
@Table(name = "board_view")
@NoArgsConstructor
public class Board {

    @Id
    private Long id;

    @Column
    private Long boardId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String title;

    @Column
    private LocalDateTime createdAt;

    @Column
    private Integer hit;

    @Column
    private String category;

    @Column
    private Integer isActive;
}
