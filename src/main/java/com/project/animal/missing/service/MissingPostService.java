package com.project.animal.missing.service;

import com.project.animal.missing.domain.MissingPost;
import com.project.animal.missing.dto.ListResponseDto;
import com.project.animal.missing.dto.MissingDetailDto;
import com.project.animal.missing.dto.MissingFilterDto;
import com.project.animal.missing.dto.MissingListResDto;
import com.project.animal.missing.exceptions.DetailNotFound;
import com.project.animal.missing.repository.MissingPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MissingPostService {

  private final MissingPostRepository missingPostRepository;

  public ListResponseDto<MissingListResDto> getPostList(MissingFilterDto filter, Pageable pageable) {
    Page<MissingPost> pages = missingPostRepository.findByFilter(filter, pageable);

    int count = (int) pages.getTotalElements();
    List<MissingListResDto> posts = pages.stream()
            .map((entity) -> MissingListResDto.fromMissingPost(entity))
            .collect(Collectors.toList());

    return new ListResponseDto<>(count, posts);
  }

  public MissingDetailDto getPostDetail(long postId) {
    Optional<MissingPost> post =  missingPostRepository.findById(postId);
    MissingPost postDetail = post.orElseThrow(() -> new DetailNotFound());
    MissingDetailDto detailDto = MissingDetailDto.fromMissingPost(postDetail);

    return detailDto;
  }


}
