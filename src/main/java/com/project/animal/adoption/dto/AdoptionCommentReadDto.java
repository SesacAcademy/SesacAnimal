package com.project.animal.adoption.dto;

import com.project.animal.adoption.domain.AdoptionComment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AdoptionCommentReadDto {

    private Long id = 0L;

    private String author;

    private String content;

    private Long parentId;

    private Long postId;

    public AdoptionCommentReadDto(AdoptionComment adoptionComment, Long postId){
        id++;
        this.author = adoptionComment.getMember().getName();
        this.content=adoptionComment.getContent();
        this.postId=postId;
    }

}
