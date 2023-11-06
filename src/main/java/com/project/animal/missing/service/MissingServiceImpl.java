package com.project.animal.missing.service;

import com.project.animal.missing.domain.MissingPost;
import com.project.animal.missing.dto.MissingListReqDto;
import com.project.animal.missing.dto.MissingListResDto;
import com.project.animal.missing.dummy.MissingPostDummy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MissingServiceImpl implements MissingService {

  public List<MissingListResDto> getPostList(MissingListReqDto dto) {
    List<MissingPost> list =  MissingPostDummy.getList();

    return list.stream()
            .map((entity) -> MissingListResDto.fromMissingPost(entity))
            .collect(Collectors.toList());
  }
}
