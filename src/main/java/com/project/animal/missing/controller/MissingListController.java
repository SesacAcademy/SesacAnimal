package com.project.animal.missing.controller;

import com.project.animal.missing.constant.EndPoint;
import com.project.animal.missing.constant.ViewName;
import com.project.animal.missing.dto.ListResponseDto;
import com.project.animal.missing.dto.MissingFilterDto;
import com.project.animal.missing.dto.MissingListEntryDto;
import com.project.animal.missing.service.MissingPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping(EndPoint.MISSING_BASE + EndPoint.LIST)
public class MissingListController extends MissingController {

  private final MissingPostService missingPostService;

  public MissingListController(MissingPostService missingPostService) {
    this.missingPostService = missingPostService;
  }

  @GetMapping
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
}
