package com.project.animal.adoption.repository;

import com.project.animal.adoption.domain.Adoption;
import com.project.animal.adoption.domain.AdoptionPostLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT distinct a FROM Adoption a LEFT JOIN FETCH a.adoptionImages JOIN FETCH a.member ORDER BY a.createdAt DESC")
    List<Adoption> findAllWithImagesAndMember();

    @Query(value = "SELECT distinct a FROM Adoption a LEFT JOIN FETCH a.adoptionImages JOIN FETCH a.member ORDER BY a.createdAt DESC",
            countQuery = "select count(a) from Adoption a")
    Page<Adoption> findAllWithImagesAndMemberPages(Pageable pageable);

    List<Adoption> findByBreedContaining(String breed);

    Page<Adoption> findByBreedContaining(String breed, Pageable pageable);

    Page<Adoption> findByCenterAndCenterAddrContaining(String center, String centerAddr, Pageable pageable);


//    @Query(value = "SELECT distinct a FROM Adoption a " +
//            "LEFT JOIN FETCH a.adoptionImages " +
//            "LEFT JOIN FETCH a.adoptionPostLikes " +
//            "JOIN FETCH a.member ORDER BY a.createdAt DESC",
//            countQuery = "SELECT COUNT(a) FROM Adoption a")
//    Page<Adoption> findAllWithImagesAndPostLikesAndMemberPages(Pageable pageable);

}
