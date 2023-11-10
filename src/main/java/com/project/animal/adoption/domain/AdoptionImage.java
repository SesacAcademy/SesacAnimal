package com.project.animal.adoption.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "adoption_image")
public class AdoptionImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adoption_image_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name= "adoption_id")
    private Adoption adoption;

    private String path;

    @Column(name = "is_active")
    private int isActive;

    public void changeAdoption(Adoption adoption) {
        this.adoption = adoption;
    }

    public void changePath(String path) {
        this.path = path;
    }

    public AdoptionImage(String serverFileName, Adoption adoption) {
        this.adoption = adoption;
        this.path = serverFileName;
        this.isActive=1;
    }
    public AdoptionImage( String serverFileName) {
        this.path = serverFileName;
        this.isActive=1;
    }

}
