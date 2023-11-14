package com.project.animal.adoption.dto;


import com.project.animal.adoption.domain.AdoptionComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.OneToMany;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
public class AdoptionCommentWriteDto {

    private Long id = 0L;

    private String author;

    private String content;

    private Long parentId;

    @OneToMany(mappedBy = "parentId")
    private List<AdoptionComment> replies; // 대댓글 리스트



//    public AdoptionCommentWriteDto(String author, String content){
//        id++;
//        this.author=author;
//        this.content=content;
//    }
//
//    public AdoptionCommentWriteDto(String author, String content, Long parentId){
//        id++;
//        this.author=author;
//        this.content=content;
//        this.parentId=parentId;
//    }
}
