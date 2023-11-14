package com.project.animal.missing.service.inf;

import com.project.animal.missing.dto.*;
import org.springframework.data.domain.Pageable;

public interface MissingPostService {
  ListResponseDto<MissingListEntryDto> getPostList(MissingFilterDto filter, Pageable pageable);

  MissingDetailDto getPostDetail(long postId);

  boolean createPost(long memberId, MissingNewDto dto);

  boolean deletePost(long postId);

  boolean editPost(MissingEditDto dto);
}
