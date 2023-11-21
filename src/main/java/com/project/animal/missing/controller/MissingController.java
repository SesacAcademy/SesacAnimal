package com.project.animal.missing.controller;

import com.project.animal.missing.constant.EndPoint;
import com.project.animal.missing.constant.ViewName;
import com.project.animal.missing.dto.ListResponseDto;
import com.project.animal.missing.dto.MissingFilterDto;
import com.project.animal.missing.dto.MissingListEntryDto;
import com.project.animal.missing.service.inf.MissingPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/v1/missing")
public class MissingController {

  private final MissingPostService missingPostService;

  @GetMapping("/list")
  public String getPostList(
          @ModelAttribute MissingFilterDto filterDto,
          @PageableDefault(sort="missing_id", direction = Sort.Direction.DESC)
          Pageable pageable,
          Model model) {

    ListResponseDto<MissingListEntryDto> result = missingPostService.getPostList(filterDto, pageable);
    Map<String, String> endPoints = createLinkConstants("detail", "list", "new");

    model.addAttribute("endPoints", endPoints);
    model.addAttribute("list", result.getList());
    model.addAttribute("count", result.getTotalCount());

    return ViewName.POST_LIST;
  }

  private Map<String, String> createLinkConstants(String ...destinations) {
    Map<String, String> endPoints = Map.of(
            "new", EndPoint.MISSING_BASE + EndPoint.NEW,
            "edit", EndPoint.MISSING_BASE + EndPoint.EDIT,
            "delete",  EndPoint.MISSING_BASE + EndPoint.DELETE,
            "detail", EndPoint.MISSING_BASE + EndPoint.DETAIL,
            "list",  EndPoint.MISSING_BASE + EndPoint.LIST,
            "newComment", EndPoint.MISSING_BASE + EndPoint.DETAIL + EndPoint.COMMENT + EndPoint.NEW,
            "editComment", EndPoint.MISSING_BASE + EndPoint.DETAIL + EndPoint.COMMENT + EndPoint.EDIT,
            "deleteComment", EndPoint.MISSING_BASE + EndPoint.DETAIL + EndPoint.COMMENT + EndPoint.DELETE
    );

    return Arrays.stream(destinations).collect(
            Collectors.toMap((d) -> d, (d) -> endPoints.get(d)));
  }
}
