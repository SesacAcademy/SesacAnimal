package com.project.animal.adoption.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdoptionCommentDto {

    private Long id;

    private String author;
    private String content;

    private Long parentId;

    public AdoptionCommentDto(String author, String content){
        id++;
        this.author=author;
        this.content=content;
    }

    public AdoptionCommentDto(String author, String content, Long parentId){
        id++;
        this.author=author;
        this.content=content;
        this.parentId=parentId;
    }
}
