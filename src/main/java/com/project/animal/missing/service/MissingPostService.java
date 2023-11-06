package com.project.animal.missing.service;

import com.project.animal.missing.domain.MissingPost;
import com.project.animal.missing.dto.MissingFilterDto;
import com.project.animal.missing.dto.MissingListResDto;
import com.project.animal.missing.dummy.MissingPostDummy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MissingPostService {

  public List<MissingListResDto> getPostList(MissingFilterDto filter, Pageable pageable) {
    List<MissingPost> list =  MissingPostDummy.getList();

    return list.stream()
            .map((entity) -> MissingListResDto.fromMissingPost(entity))
            .collect(Collectors.toList());
  }
}
