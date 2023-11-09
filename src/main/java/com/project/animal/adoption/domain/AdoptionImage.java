package com.project.animal.adoption.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;

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

//    @ElementCollection
//    @CollectionTable(name = "adoption_image", joinColumns = @JoinColumn(name = "adoption_id"))
//    @Column(name = "path")
    private String path;

    public AdoptionImage(String serverFileName, Adoption adoption) {
        this.adoption = adoption;
        this.path = serverFileName;
    }
    public AdoptionImage( String serverFileName) {
        this.path = serverFileName;
    }

}
