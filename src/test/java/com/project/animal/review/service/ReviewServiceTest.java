package com.project.animal.review.service;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import com.project.animal.member.domain.Member;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.dto.CreateReviewPostDto;
import com.project.animal.review.dto.ReadList;
import com.project.animal.review.repository.ReviewPostCustomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewPostCustomRepository reviewPostCustomRepository;

    @InjectMocks
    private ReviewService reviewService;


    @DisplayName("검색을 통한 게시글 검색")
    @Test
    void readByKeyword_서비스_테스트() {
        // Given
        String type = "someType";
        Integer page = 0;
        int size = 10;
        String keyword = "someKeyword";
        List<ReviewPost> reviewPosts = IntStream.range(0, size)
                .mapToObj(i -> {
                    // 테스트용 DTO 객체 생성
                    CreateReviewPostDto createReviewPostDto = new CreateReviewPostDto();
                    createReviewPostDto.setContent("Sample content " + i);
                    createReviewPostDto.setTitle("Sample title " + i);

                    // 테스트용 Member 객체 생성
                    Member member = new Member(); // 실제 생성자에 필요한 매개변수를 제공해야 합니다.

                    // ReviewPost 객체 생성
                    return new ReviewPost(createReviewPostDto, member);
                })
                .collect(Collectors.toList());

        Page<ReviewPost> pagedResponse = new PageImpl<>(reviewPosts, PageRequest.of(page, size), reviewPosts.size());

        given(reviewPostCustomRepository.findAllByKeyword(eq(type), eq(keyword), any(Pageable.class)))
                .willReturn(pagedResponse);

        // When
        ReadList readList = reviewService.readByKeyword(type, page, size, keyword);

        // Then
        assertNotNull(readList, "ReadList should not be null");
        assertEquals(size, readList.getList().size(), "The size of the response should match the number of review posts");
    }


}
