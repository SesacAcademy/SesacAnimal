package com.project.animal.adoption.repository;

import com.project.animal.adoption.domain.Adoption;
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


//    @Query("SELECT distinct a FROM Adoption a LEFT JOIN FETCH a.adoptionImages JOIN FETCH a.member ORDER BY a.createdAt DESC")
//    List<Adoption> findAllWithImagesAndMemberWithLimitAndOffset(@Param("limit") int boardSize, @Param("offset") int startRow);

//    @Query(value = "SELECT * FROM adoption a LEFT JOIN adoption_image i ON a.adoption_id = i.adoption_id LEFT JOIN member m ON a.member_id = m.member_id ORDER BY a.created_at DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
//    List<Adoption> findAllWithImagesAndMemberWithLimitAndOffset(@Param("limit") int limit, @Param("offset") int offset);
}
