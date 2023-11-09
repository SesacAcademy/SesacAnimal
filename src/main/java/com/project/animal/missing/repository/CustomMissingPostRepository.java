package com.project.animal.missing.repository;

import com.project.animal.missing.domain.MissingPost;
import com.project.animal.missing.dto.MissingFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomMissingPostRepository {
  Page<MissingPost> findByFilter(MissingFilterDto filter, Pageable pageable);
}
