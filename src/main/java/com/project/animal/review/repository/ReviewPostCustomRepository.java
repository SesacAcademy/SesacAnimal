package com.project.animal.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class ReviewPostCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Autowired
    public ReviewPostCustomRepository(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }
}
