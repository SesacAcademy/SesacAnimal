package com.project.animal.adoption.repository;

import com.project.animal.adoption.domain.Adoption;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {


    @Query("SELECT a FROM Adoption a LEFT JOIN FETCH a.adoptionImages WHERE a.id = :id ORDER BY a.createdAt DESC")
    Adoption findByIdWithImage(@Param("id") Long id);

    @Query("SELECT a FROM Adoption a LEFT JOIN FETCH a.adoptionImages JOIN FETCH a.member WHERE a.id = :id ORDER BY a.createdAt DESC")
    Adoption findByIdWithImageAndMember(@Param("id") Long id);

    @Query("SELECT a FROM Adoption a LEFT JOIN FETCH a.adoptionImages ORDER BY a.createdAt DESC")
    List<Adoption> findAllWithImages();

//    @Query("SELECT a FROM Adoption a JOIN FETCH a.adoptionImages JOIN FETCH a.member")
    @Query("SELECT a FROM Adoption a LEFT JOIN FETCH a.adoptionImages JOIN FETCH a.member ORDER BY a.createdAt DESC")
    List<Adoption> findAllWithImagesAndMember();


}
