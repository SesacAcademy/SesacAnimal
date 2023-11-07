package com.project.animal.adoption.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "temp_image")
public class TempImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temp_image_id")
    private Long id;

    @Column( name = "image_url")
    private String imageUrl;
}
