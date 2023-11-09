package com.project.animal.adoption.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AdoptionWriteDto {

    private String author;

    private String title;

    private MultipartFile image;

    private String breed;

    private String gender;

    private String age;

    private String center;

    private String color;

    private String happenPlace;

    private String neutered;

    private String specialMark;

    private String content;

    private String status;


}
