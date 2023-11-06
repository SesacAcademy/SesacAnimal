package com.project.animal.missing.controller;

import com.project.animal.missing.constant.EndPoint;
import com.project.animal.missing.constant.ViewName;
import com.project.animal.missing.dto.MissingListReqDto;
import com.project.animal.missing.dto.MissingListResDto;
import com.project.animal.missing.dummy.MissingPostDummy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.text.View;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(EndPoint.MISSING)
public class MissingController {

  @GetMapping(EndPoint.LIST)
  public String getPostList(@Valid MissingListReqDto dto, BindingResult br, Model model) {
    List<MissingListResDto> list = MissingPostDummy.getDummyDto();
    model.addAttribute("list", list);
    model.addAttribute("count", 100);

    return ViewName.POST_LIST;
  }

  @GetMapping(EndPoint.DETAIL)
  public String getPostDetail(Model model) {
    List<MissingListResDto> list = MissingPostDummy.getDummyDto();
    String[] comments = {"test1", "comments2"};

    model.addAttribute("detail", list.get(0));
    model.addAttribute("comments", comments);

    return ViewName.POST_DETAIL;
  }

  @GetMapping(EndPoint.NEW)
  public String getPostNew() {
    return ViewName.POST_NEW;
  }

}
