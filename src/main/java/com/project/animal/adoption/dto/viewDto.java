package com.project.animal.adoption.dto;

import com.project.animal.adoption.domain.Adoption;

public class viewDto {
    private String title;
    private String content;
    private String url;
    public viewDto(Adoption adoption){
        this.title =adoption.getTitle();
        this.content = adoption.getContent();
        this.url = adoption.getAdoptionImage().getPath();
    }
}
