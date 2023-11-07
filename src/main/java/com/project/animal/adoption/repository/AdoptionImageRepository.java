package com.project.animal.adoption.repository;

import com.project.animal.adoption.domain.Adoption;
import com.project.animal.adoption.domain.AdoptionImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdoptionImageRepository extends JpaRepository<AdoptionImage, Long> {

}
