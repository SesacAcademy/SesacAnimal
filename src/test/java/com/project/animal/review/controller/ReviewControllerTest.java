package com.project.animal.review.controller;

import com.project.animal.review.constant.ViewName;
import com.project.animal.review.dto.ReadList;
import com.project.animal.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = ReviewController.class, useDefaultFilters = false)
public class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;
    @InjectMocks
    ReviewController reviewController;

    private MockMvc mockMvc;

    @Mock
    private Model model;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    @Test
    @WithMockUser
    @DisplayName("검색어를 통한 페이지 불러오기")
    public void testReadBySearch() {
        // Given
        String type = "someType";
        String keyword = "someKeyword";
        Integer page = 1;

        ReadList mockReadList = new ReadList();
        when(reviewService.readByKeyword(eq(type), eq(page), anyInt(), eq(keyword))).thenReturn(mockReadList);

        // When
        String viewName = reviewController.readBySearch(type, keyword, page, model);

        // Then
        assertEquals(ViewName.REVIEW_LIST_BY_SEARCH, viewName);

        verify(model).addAttribute("listDto", mockReadList);
        verify(model).addAttribute("type", type);
        verify(model).addAttribute("keyword", keyword);

        verify(reviewService).readByKeyword(eq(type), eq(page), anyInt(), eq(keyword));
    }
    @Test
    @DisplayName("필터를 통한 페이지 불러오기")
    public void testReadByFilter() {
        // Given
        String type = "exampleType";
        int page = 1;
        Model model = mock(Model.class);
        ReadList mockReadList = new ReadList();
        when(reviewService.readByFilter(eq(type), eq(page), anyInt())).thenReturn(mockReadList);

        // When
        String result = reviewController.readByFilter(type, page, model);

        // Then
        verify(model).addAttribute(eq("listDto"), eq(mockReadList));
        verify(model).addAttribute(eq("type"), eq(type));
        assertEquals(ViewName.REVIEW_LIST_BY_FILTER, result);
    }


}
