package com.project.animal.missing.repository;

import com.project.animal.missing.domain.MissingPost;
import com.project.animal.missing.domain.QMissingPostImage;
import com.project.animal.missing.dto.MissingFilterDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.project.animal.missing.domain.QMissingPost;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CustomMissingPostRepositoryImpl implements CustomMissingPostRepository {

  private final int isActive = 1;
  private final JPAQueryFactory queryFactory;

  private final QMissingPost qMissing = QMissingPost.missingPost;
  private final QMissingPostImage qImage = QMissingPostImage.missingPostImage;


  @Override
  public Page<MissingPost> findByFilter(MissingFilterDto filter, Pageable pageable) {

    List<MissingPost> results = queryFactory
            .selectFrom(qMissing)
            .innerJoin(qMissing.images, qImage).fetchJoin()
            .where(
                    eqAnimalType(filter.getAnimalType()),
                    containKeyword(filter.getSearch()),
                    goeFromDate(filter.getFromDate()),
                    loeEndDate(filter.getEndDate()),
                    eqIsActive(isActive))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(qMissing.updatedAt.desc())
            .fetch();

    long total = queryFactory
            .select(qMissing.missingId.count())
            .where(
                    eqAnimalType(filter.getAnimalType()),
                    containKeyword(filter.getSearch()),
                    goeFromDate(filter.getFromDate()),
                    loeEndDate(filter.getEndDate()),
                    eqIsActive(isActive))
            .from(qMissing)
            .fetchOne();

    return new PageImpl<>(results, pageable, total);
  }

  private BooleanExpression eqAnimalType(String animalType) {
    if (StringUtils.isNullOrEmpty(animalType)) {
      return null;
    }
    return qMissing.animalType.equalsIgnoreCase(animalType);
  }

  private BooleanExpression containKeyword(String keyword) {
    if (StringUtils.isNullOrEmpty(keyword)) {
      return null;
    }
    return qMissing.title.containsIgnoreCase(keyword);
  }

  private BooleanExpression loeEndDate(LocalDateTime endDate) {
    if (endDate == null) {
      return null;
    }
    return qMissing.missingTime.loe(endDate);
  }

  private BooleanExpression goeFromDate(LocalDateTime fromDate) {
    if (fromDate == null) {
      return null;
    }
    return qMissing.missingTime.goe(fromDate);
  }

  private BooleanExpression eqIsActive(int status) {
    return qMissing.isActive.eq(status);
  }

}
