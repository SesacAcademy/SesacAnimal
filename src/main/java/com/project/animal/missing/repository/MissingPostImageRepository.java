package com.project.animal.missing.repository;

import com.project.animal.missing.domain.MissingPostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissingPostImageRepository extends JpaRepository<MissingPostImage, Long> {
}
