package com.project.animal.missing.repository;

import com.project.animal.missing.domain.MissingPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissingPostRepository extends JpaRepository<MissingPost, Long>, CustomMissingPostRepository {
}
