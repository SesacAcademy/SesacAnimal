package com.project.animal.adoption.repository;

import com.project.animal.adoption.domain.Adoption;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {

    default void plusView(Adoption adoption) {
        int hit = adoption.getHit();
        ++hit;

        adoption.setHit(hit);

    }
    @Query("SELECT a FROM Adoption a JOIN FETCH a.adoptionImage WHERE a.id = :id")
    Adoption findByIdWithImage(@Param("id") Long id);

    @Query("SELECT a FROM Adoption a JOIN FETCH a.adoptionImage")
    List<Adoption> findAllWithImages();
}
