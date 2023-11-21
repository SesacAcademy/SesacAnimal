package com.project.animal.missing.controller;


import com.project.animal.global.common.annotation.Member;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.missing.dto.like.MissingLikeDto;
import com.project.animal.missing.service.inf.MissingLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/missing/like")
public class MissingLikeController {

  private final MissingLikeService missingLikeService;


  @PostMapping
  public Map<String ,Integer> likePost(@Member MemberDto member, @RequestBody MissingLikeDto dto) {
    int isLiked = missingLikeService.likePost(member.getId(), dto.getPostId());

    return Map.of("isLiked", isLiked);
  }
}
