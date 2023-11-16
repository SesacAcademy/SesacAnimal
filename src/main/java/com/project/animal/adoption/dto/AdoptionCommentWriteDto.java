package com.project.animal.adoption.dto;


import com.project.animal.adoption.domain.AdoptionComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
public class AdoptionCommentWriteDto {

    private Long id = 0L;

    private String author;

    @NotNull
    @NotBlank
    private String content;

    private Long parentId;

    @OneToMany(mappedBy = "parentId")
    private List<AdoptionComment> replies; // 대댓글 리스트



}
