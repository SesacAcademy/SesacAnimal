package com.project.animal.missing.service;


import com.project.animal.member.repository.MemberRepository;
import com.project.animal.missing.domain.MissingPost;
import com.project.animal.missing.dto.ListResponseDto;
import com.project.animal.missing.dto.MissingFilterDto;
import com.project.animal.missing.dto.MissingListEntryDto;
import com.project.animal.missing.repository.MissingPostRepository;
import com.project.animal.missing.service.converter.DtoEntityConverter;
import com.project.animal.missing.service.inf.MissingLikeService;
import com.project.animal.missing.service.inf.MissingPostImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MissingPostServiceTest {

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private MissingPostRepository missingPostRepository;

  @Mock
  private MissingPostImageService missingPostImageService;

  @Mock
  private MissingLikeService missingLikeService;

  @Mock
  private DtoEntityConverter dtoEntityConverter;

  @InjectMocks
  private MissingPostServiceImpl postService;


  @Nested
  @DisplayName("목록 가져오기 메소드 테스트")
  class GetPostListTest {

    @Test
    @DisplayName("성공시 페이지 사이즈 이하의 목록을 반환")
    public void return_total_count_and_list_based_on_page_size() {
      MissingFilterDto mockFilterDto = new MissingFilterDto();
      Pageable mockPageable = PageRequest.of(0, 10);
      Page<MissingPost> dummyResult = createDummyRepositoryResult(mockPageable);
      List<Integer> dummyLikeCounts = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

      when(missingPostRepository.findByFilter(mockFilterDto, mockPageable)).thenReturn(dummyResult);
      when(dtoEntityConverter.toMissingListEntryDto(any(MissingPost.class))).thenReturn(new MissingListEntryDto());
      when(missingLikeService.getLikeCountMultiple(anyList())).thenReturn(dummyLikeCounts);
      ListResponseDto<MissingListEntryDto> result = postService.getPostList(mockFilterDto, mockPageable);

      assertThat(result.getTotalCount()).isLessThanOrEqualTo(mockPageable.getPageSize());
      assertThat(result.getList().size()).isLessThanOrEqualTo(mockPageable.getPageSize());
    }

    @Test
    @DisplayName("레포지토리에서 예외 발생시 예외 전파")
    public void deliver_exception_when_repository_throws() {
      MissingFilterDto mockFilterDto = new MissingFilterDto();
      Pageable mockPageable = PageRequest.of(0, 10);

      when(missingPostRepository.findByFilter(mockFilterDto, mockPageable)).thenThrow(RuntimeException.class);
      assertThrows(RuntimeException.class, () -> postService.getPostList(mockFilterDto, mockPageable));
    }

    private Page<MissingPost> createDummyRepositoryResult(Pageable pageable) {
      List<MissingPost> missingPosts = IntStream.range(0, pageable.getPageSize())
              .mapToObj(i -> new MissingPost())
              .collect(Collectors.toList());
      return new PageImpl<>(missingPosts, pageable, missingPosts.size());
    }
  }
}
