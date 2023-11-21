package com.project.animal.missing.controller;

import com.project.animal.global.common.annotation.Member;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.missing.constant.EndPoint;
import com.project.animal.missing.constant.ViewName;
import com.project.animal.missing.dto.ListResponseDto;
import com.project.animal.missing.dto.MissingFilterDto;
import com.project.animal.missing.dto.MissingListEntryDto;
import com.project.animal.missing.dto.MissingNewDto;
import com.project.animal.missing.exception.InvalidCreateFormException;
import com.project.animal.missing.service.inf.MissingPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/v1/missing")
public class MissingController {

  public final int SUCCESS_FLAG = 1;

  public final int FAIL_FLAG = 0;

  private final MissingPostService missingPostService;

  @GetMapping("/list")
  public String getPostList(
          @ModelAttribute MissingFilterDto filterDto,
          @Member MemberDto member,
          @PageableDefault(sort="missing_id", direction = Sort.Direction.DESC)
          Pageable pageable,
          Model model) {

    ListResponseDto<MissingListEntryDto> result = missingPostService.getPostList(filterDto, pageable);
    Map<String, String> endPoints = createLinkConstants("detail", "list", "new");

    model.addAttribute("endPoints", endPoints);
    model.addAttribute("list", result.getList());
    model.addAttribute("count", result.getTotalCount());
    model.addAttribute("member", member);

    return ViewName.POST_LIST;
  }

  @GetMapping("/new")
  public String getPostNew(@Valid @ModelAttribute("post") MissingNewDto dto, BindingResult br, Model model) {
    Map<String, String> endPoints = createLinkConstants("new", "list");

    model.addAttribute("endPoints", endPoints);
    return ViewName.POST_NEW;
  }

  @PostMapping("/new")
  public String handleCreateRequest(
          @Valid @ModelAttribute("post") MissingNewDto dto, BindingResult br, RedirectAttributes redirectAttributes,
          @Member MemberDto member) {

    if (br.hasErrors() || member == null) {
      throw new InvalidCreateFormException(dto, br);
    }

    boolean isSuccess = missingPostService.createPost(member.getId(), dto);
    redirectAttributes.addFlashAttribute("isSuccess", isSuccess ? SUCCESS_FLAG : FAIL_FLAG);
    redirectAttributes.addFlashAttribute("isRedirected", SUCCESS_FLAG);

    return "redirect:" + EndPoint.MISSING_BASE + EndPoint.NEW;
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
