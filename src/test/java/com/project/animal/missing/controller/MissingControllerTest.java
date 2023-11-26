package com.project.animal.missing.controller;

import com.google.common.primitives.Ints;
import com.project.animal.global.common.provider.JwtTokenProvider;
import com.project.animal.missing.controller.utils.PathMaker;
import com.project.animal.missing.dto.ListResponseDto;
import com.project.animal.missing.dto.MissingFilterDto;
import com.project.animal.missing.dto.MissingListEntryDto;
import com.project.animal.missing.dto.image.MissingPostImageDto;
import com.project.animal.missing.service.inf.MissingPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(MissingController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MissingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private JwtTokenProvider jwtTokenProvider;

  @MockBean
  private MissingPostService missingPostService;

  @MockBean
  private PathMaker pathMaker;


  @Nested
  @DisplayName("실종 게시판 목록 페이지 불러오기 테스트 클래스")
  class GetPostList {
    private final String ENDPOINT = "/v1/missing/list";
    private final String HTML_TYPE = MediaType.TEXT_HTML + ";charset=" + StandardCharsets.UTF_8;
    private final Map<String, String> dummyPath = Map.of(
            "list", "dummy",
            "detail", "dummy",
            "new", "dummy"
    );

    @Test
    @DisplayName("서비스 레이어가 예외를 던지는 경우")
    public void response_error_html_when_service_throws() throws Exception {
      MissingFilterDto mockFilterDto = new MissingFilterDto();
      Pageable mockPageable = PageRequest.of(0, 10);

      when(pathMaker.createLink("detail", "list", "new")).thenReturn(dummyPath);
      when(missingPostService.getPostList(any(MissingFilterDto.class), any(Pageable.class))).thenThrow(RuntimeException.class);

      mockMvc.perform(get(ENDPOINT))
              .andExpect(status().isOk())
              .andExpect(content().contentType(HTML_TYPE))
              .andExpect(content().string(containsString("500")));
    }

    @Test
    @DisplayName("페이지넘버 및 필터 쿼리 없이 요청이 들어온 케이스")
    public void response_html_with_default_page_number_and_size() throws Exception {

      MissingFilterDto mockFilterDto = new MissingFilterDto();
      Pageable mockPageable = PageRequest.of(0, 10);

      int dummyCount = 10;
      ListResponseDto<MissingListEntryDto> mockResponse = createDummyServiceResult(dummyCount);

      when(pathMaker.createLink("detail", "list", "new")).thenReturn(dummyPath);
      when(missingPostService.getPostList(mockFilterDto, mockPageable)).thenReturn(mockResponse);

      mockMvc.perform(get(ENDPOINT))
              .andExpect(status().isOk())
              .andExpect(content().contentType(HTML_TYPE))
              .andExpect(model().attribute("count", dummyCount))
              .andExpect(model().attribute("list", mockResponse.getList()));
    }

    @Test
    @DisplayName("페이지넘버 및 필터 쿼리를 추가하여 요청이 들어온 케이스")
    public void response_html_with_filtered_posts() throws Exception {
      final String KEYWORD = "sampleKeyword";
      final int PAGE = 2;
      final int SIZE = 10;

      int dummyCount = 10;
      ListResponseDto<MissingListEntryDto> mockResponse = createDummyServiceResult(dummyCount);

      when(pathMaker.createLink("detail", "list", "new")).thenReturn(dummyPath);
      when(missingPostService.getPostList(any(MissingFilterDto.class), any(Pageable.class))).thenReturn(mockResponse);

      mockMvc.perform(get(ENDPOINT)
                      .param("page", String.valueOf(PAGE))
                      .param("size", String.valueOf(SIZE))
                      .param("search", KEYWORD)
                      .param("animalType", "")
                      .param("specifics", "")
                      .param("color", "")
                      .param("fromDate", "")
                      .param("endDate", ""))
              .andExpect(status().isOk())
              .andExpect(content().contentType(HTML_TYPE))
              .andExpect(model().attribute("count", dummyCount))
              .andExpect(model().attribute("list", mockResponse.getList()));
    }

    private ListResponseDto<MissingListEntryDto> createDummyServiceResult(int count) {
      List<MissingListEntryDto> mockPosts = IntStream
              .range(0, count)
              .mapToObj((n) -> new MissingListEntryDto())
              .collect(Collectors.toList());

      List<MissingPostImageDto> imageDto = List.of(new MissingPostImageDto());
      mockPosts.forEach((dto) -> dto.addImages(imageDto));

      return new ListResponseDto<>(count, mockPosts);
    }
  }
}
