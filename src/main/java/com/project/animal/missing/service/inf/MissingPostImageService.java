package com.project.animal.missing.service.inf;

import com.project.animal.missing.domain.MissingPost;
import com.project.animal.missing.domain.MissingPostImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MissingPostImageService {
  List<MissingPostImage> createImage(MultipartFile[] images, MissingPost post);
}
