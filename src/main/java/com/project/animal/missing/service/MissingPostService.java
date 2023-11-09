package com.project.animal.missing.service;

import com.project.animal.missing.domain.MissingPost;
import com.project.animal.missing.dto.*;
import com.project.animal.missing.exceptions.DetailNotFoundException;
import com.project.animal.missing.exceptions.PostSaveFailException;
import com.project.animal.missing.repository.MissingPostRepository;
import com.project.animal.missing.service.converter.DtoEntityConverter;
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

  private final DtoEntityConverter converter;

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
    MissingPost postDetail = post.orElseThrow(() -> new DetailNotFoundException());
    MissingDetailDto detailDto = MissingDetailDto.fromMissingPost(postDetail);

    return detailDto;
  }

  public boolean createPost(MissingNewDto dto) {
    try {
      MissingPost post = converter.toMissingPost(dto);
      MissingPost result = missingPostRepository.save(post);

      if (result == null) throw new Exception();
      return true;

    } catch (Exception ex) {
      log.error("Error in createPost: >> " + dto.toString());
      throw new PostSaveFailException(ex.getMessage(), ex.getCause(), dto);
    }
  }


}
