package com.project.animal.missing.repository;

import com.project.animal.missing.domain.MissingPost;
import com.project.animal.missing.domain.QMissingPostImage;
import com.project.animal.missing.dto.MissingFilterDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.project.animal.missing.domain.QMissingPost;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
public class CustomMissingPostRepositoryImpl implements CustomMissingPostRepository {

  private final int isActive = 1;
  private final JPAQueryFactory queryFactory;

  @Autowired
  public CustomMissingPostRepositoryImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Page<MissingPost> findByFilter(MissingFilterDto filter, Pageable pageable) {
    QMissingPost qMissing = QMissingPost.missingPost;
    QMissingPostImage qImage = QMissingPostImage.missingPostImage;

    BooleanBuilder builder = new BooleanBuilder();

    builder.and(qMissing.isActive.eq(isActive));

    if (filter.getAnimalType() != null) {
      builder.and(qMissing.animalType.equalsIgnoreCase(filter.getAnimalType()));
    }

    // TODO: fromDate로 조건 검색시 일치하지 않음
    if (filter.getFromDate() != null) {
      builder.and(qMissing.missingTime.goe(filter.getFromDate()));
    }

    if (filter.getEndDate() != null) {
      builder.and(qMissing.missingTime.loe(filter.getEndDate()));
    }

    // NOTE: 검색은 타이틀 기준으로 실행
    if (filter.getSearch() != null && !filter.getSearch().isBlank() && !filter.getSearch().isEmpty()) {
      builder.and(qMissing.title.containsIgnoreCase(filter.getSearch()));
    }

    List<MissingPost> results = queryFactory
            .selectFrom(qMissing)
            .where(builder)
            .innerJoin(qMissing.images, qImage)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy()
            .fetch();

    long total = queryFactory
            .select(qMissing.missingId.count())
            .where(qMissing.isActive.eq(isActive))
            .from(qMissing)
            .fetchOne();

    return new PageImpl<>(results, pageable, total);
  }
}
