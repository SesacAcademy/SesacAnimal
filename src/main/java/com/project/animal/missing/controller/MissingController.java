package com.project.animal.missing.controller;

import com.project.animal.global.common.annotation.Member;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.missing.constant.ViewName;
import com.project.animal.missing.controller.utils.PathMaker;
import com.project.animal.missing.dto.*;
import com.project.animal.missing.dto.comment.MissingCommentListEntryDto;
import com.project.animal.missing.exception.InvalidCreateFormException;
import com.project.animal.missing.exception.InvalidEditFormException;
import com.project.animal.missing.service.inf.MissingPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/v1/missing")
public class MissingController {

  public final int SUCCESS_FLAG = 1;

  public final int FAIL_FLAG = 0;

  private final MissingPostService missingPostService;

  private final PathMaker pathMaker;

  @GetMapping("/list")
  public String getPostList(
          @ModelAttribute MissingFilterDto filterDto,
          @Member MemberDto member,
          @PageableDefault(size = 10)
          Pageable pageable,
          Model model) {
    Pageable adjustedPageable = PageRequest.of(
            pageable.getPageNumber() != 0 ? pageable.getPageNumber() - 1 : 0,
            pageable.getPageSize(),
            pageable.getSort());
    ListResponseDto<MissingListEntryDto> result = missingPostService.getPostList(filterDto, adjustedPageable);
    Map<String, String> endPoints = pathMaker.createLink("detail", "list", "new");

    model.addAttribute("endPoints", endPoints);
    model.addAttribute("list", result.getList());
    model.addAttribute("count", result.getTotalCount());
    model.addAttribute("member", member);

    return ViewName.POST_LIST;
  }

  @GetMapping("/detail/{postId}")
  public String getPostDetail(@PathVariable("postId") long postId,
                              @Member MemberDto member,
                              Model model) {

    long memId = member != null ? member.getId() : 0;
    MissingDetailDto detail = missingPostService.getPostDetail(postId, memId);
    List<MissingCommentListEntryDto> comments = detail.getComments();

    Map<String, String> endPoints = pathMaker.createLink("edit", "delete", "newComment", "editComment", "deleteComment");

    model.addAttribute("selfId", memId);
    model.addAttribute("endPoints", endPoints);
    model.addAttribute("post", detail);
    model.addAttribute("comments", comments);


    return ViewName.POST_DETAIL;
  }


  @GetMapping("/new")
  public String getPostNew(@Valid @ModelAttribute("post") MissingNewDto dto, BindingResult br, Model model) {
    Map<String, String> endPoints = pathMaker.createLink("new", "list");

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

    return "redirect:" + "/v1/missing/new";
  }

  @GetMapping("/edit/{postId}")
  public String showEditView(@PathVariable("postId") long postId, Model model, @Member MemberDto memberDto) {
    MissingDetailDto detail = missingPostService.getPostDetail(postId, memberDto.getId());

    if (detail.getMemberId() != memberDto.getId()) {
      throw new RuntimeException("수정이 불가한 유저 입니다.");
    }

    Map<String, String> endPoints = pathMaker.createLink("edit");
    model.addAttribute("detail", detail);
    model.addAttribute("endPoints", endPoints);

    return ViewName.POST_EDIT;
  }

  @PutMapping("/edit/{postId}")
  public String handleEditRequest(
          @Valid @ModelAttribute("detail") MissingEditDto dto,
          BindingResult br,
          RedirectAttributes redirectAttributes,
          @Member MemberDto member) {

    if (br.hasErrors() || member == null) {
      throw new InvalidEditFormException(dto, br);
    }

    boolean isSuccess = missingPostService.editPost(member.getId(), dto);
    redirectAttributes.addFlashAttribute("isSuccess", isSuccess ? SUCCESS_FLAG : FAIL_FLAG);
    redirectAttributes.addFlashAttribute("isRedirected", SUCCESS_FLAG);

    return "redirect:" + "/v1/missing/detail/" + dto.getMissingId();
  }

  @DeleteMapping("/delete/{postId}")
  public String handleDeleteRequest(@PathVariable("postId") long id, RedirectAttributes redirectAttributes) {
    // TODO: id 없는 경우 검증
    boolean result = missingPostService.deletePost(id);
    redirectAttributes.addFlashAttribute("serverMsg", "삭제에 성공하였습니다.");

    return "redirect:" + "/v1/missing/list";
  }

}
