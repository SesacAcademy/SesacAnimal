package com.project.animal.missing.service;

import com.project.animal.missing.dto.MissingListReqDto;
import com.project.animal.missing.dto.MissingListResDto;

import java.util.List;

public interface MissingService {
  List<MissingListResDto> getPostList(MissingListReqDto dto);
}
