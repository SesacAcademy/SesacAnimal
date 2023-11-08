package com.project.animal.missing.controller;

import com.project.animal.missing.constant.EndPoint;
import com.project.animal.missing.constant.ViewName;
import com.project.animal.missing.dto.*;
import com.project.animal.missing.exception.*;
import com.project.animal.missing.service.MissingPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping(EndPoint.MISSING_BASE)
public class MissingController {

  private final int SUCCESS_FLAG = 1;

  private final int FAIL_FLAG = 0;

  private final MissingPostService missingPostService;

  @Autowired
  public MissingController(MissingPostService missingPostService) {
    this.missingPostService = missingPostService;
  }


  @GetMapping(EndPoint.LIST)
  public String getPostList(
          @ModelAttribute MissingFilterDto filterDto,
          @PageableDefault(sort="missing_id", direction = Sort.Direction.DESC)
          Pageable pageable,
          Model model) {

    ListResponseDto<MissingListResDto> result = missingPostService.getPostList(filterDto, pageable);
    Map<String, String> endPoints = createLinkConstants("delete", "list");

    model.addAttribute("endPoints", endPoints);
    model.addAttribute("list", result.getList());
    model.addAttribute("count", result.getTotalCount());

    return ViewName.POST_LIST;
  }


  @GetMapping(EndPoint.DETAIL + EndPoint.PATH_ID)
  public String getPostDetail(@PathVariable(EndPoint.ID_KEY) long postId, Model model) {
    MissingDetailDto detail = missingPostService.getPostDetail(postId);
    String[] comments = {"test1", "comments2"};

    Map<String, String> endPoints = createLinkConstants("edit", "delete");

    model.addAttribute("endPoints", endPoints);
    model.addAttribute("detail", detail);
    model.addAttribute("comments", comments);

    return ViewName.POST_DETAIL;
  }

  @GetMapping(EndPoint.NEW)
  public String getPostNew(@Valid @ModelAttribute("post") MissingNewDto dto, BindingResult br) {
    return ViewName.POST_NEW;
  }

  @PostMapping(EndPoint.CREATE)
  public String handleCreateRequest(@Valid @ModelAttribute("post") MissingNewDto dto, BindingResult br, RedirectAttributes redirectAttributes) {
    if (br.hasErrors()) {
      throw new InvalidCreateFormException(dto, br);
    }

    boolean isSuccess = missingPostService.createPost(dto);
    redirectAttributes.addFlashAttribute("isSuccess", isSuccess ? SUCCESS_FLAG : FAIL_FLAG);
    redirectAttributes.addFlashAttribute("isRedirected", SUCCESS_FLAG);

    return "redirect:" + EndPoint.MISSING + EndPoint.NEW;
  }

  @GetMapping(EndPoint.EDIT + EndPoint.PATH_ID)
  public String showEditView(@PathVariable(EndPoint.ID_KEY) long postId, Model model) {
    MissingDetailDto detail = missingPostService.getPostDetail(postId);
    model.addAttribute("detail", detail);

    return ViewName.POST_EDIT;
  }

  @PutMapping(EndPoint.EDIT  + EndPoint.PATH_ID)
  public String handleEditRequest(@Valid @ModelAttribute("detail") MissingEditDto dto, BindingResult br, RedirectAttributes redirectAttributes) {
    if (!br.hasErrors()) {
      throw new InvalidEditFormException(dto, br);
    }

    boolean isSuccess = missingPostService.editPost(dto);
    redirectAttributes.addFlashAttribute("isSuccess", isSuccess ? SUCCESS_FLAG : FAIL_FLAG);
    redirectAttributes.addFlashAttribute("isRedirected", SUCCESS_FLAG);

    return "redirect:" + EndPoint.MISSING + "/" + dto.getMissingId();
  }

  @DeleteMapping(EndPoint.DELETE + EndPoint.PATH_ID)
  public String handleDeleteRequest(@PathVariable(EndPoint.ID_KEY) long id, RedirectAttributes redirectAttributes) {

    boolean result = missingPostService.deletePost(id);
    redirectAttributes.addFlashAttribute("serverMsg", "Success to delete the post");

    return "redirect:" + EndPoint.MISSING_BASE + EndPoint.LIST;
  }

  private Map<String, String> createLinkConstants(String ...destinations) {
    Map<String, String> endPoints = Map.of(
            "edit", EndPoint.MISSING_BASE + EndPoint.EDIT,
            "delete",  EndPoint.MISSING_BASE + EndPoint.DELETE,
            "detail", EndPoint.MISSING_BASE,
            "list",  EndPoint.MISSING_BASE + EndPoint.LIST
    );

   return Arrays.stream(destinations).collect(
           Collectors.toMap((d) -> d, (d) -> endPoints.get(d)));
  }

}
