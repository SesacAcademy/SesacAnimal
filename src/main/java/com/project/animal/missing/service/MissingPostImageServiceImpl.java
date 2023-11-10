package com.project.animal.missing.service;

import com.project.animal.global.common.dto.ImageListDto;
import com.project.animal.global.common.minioserviceprovider.ImageUploadMinio;
import com.project.animal.missing.domain.MissingPost;
import com.project.animal.missing.domain.MissingPostImage;
import com.project.animal.missing.repository.MissingPostImageRepository;
import com.project.animal.missing.service.inf.MissingPostImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MissingPostImageServiceImpl implements MissingPostImageService {

  private final MissingPostImageRepository missingPostImageRepository;

  private final ImageUploadMinio imageUploadMinio;

  public List<MissingPostImage> createImage(MultipartFile[] images, MissingPost post) {

    ImageListDto imageDto = new ImageListDto(Arrays.stream(images).toList());
    List<String> urls= imageUploadMinio.insertImageMinio(imageDto, imageUploadMinio.MISSING);

    return urls.stream()
            .map((url) -> new MissingPostImage(url, post))
            .map(missingPostImageRepository :: save)
            .collect(Collectors.toList());
  }
}