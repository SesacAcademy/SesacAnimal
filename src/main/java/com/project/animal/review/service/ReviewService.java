package com.project.animal.review.service;

import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.member.domain.Member;
import com.project.animal.member.repository.MemberRepository;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.dto.*;
import com.project.animal.review.exception.NotFoundException;
import com.project.animal.review.exception.ReviewDtoNullException;
import com.project.animal.review.repository.ReviewPostCustomRepository;
import com.project.animal.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final MemberRepository memberRepository;

    private final ReviewPostCustomRepository reviewPostCustomRepository;

    /**
     * 게시글 작성을 담당하는 Service
     *
     * @version 0.1
     * @author 손승범
     * @Param Member MeberDto 객체
    * */
    public ReviewPost createReviewPost(CreateReviewPostDto createReviewPostDto, MemberDto memberDto) {
        dtoCheck(createReviewPostDto);
        Member member = findMemberById(memberDto);
        ReviewPost reviewPost = new ReviewPost(createReviewPostDto, member);
        return reviewRepository.save(reviewPost);
    }

    private Pageable createPageByCreatedAt(int page, int size){
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        return PageRequest.of(page, size, sort);
    }

    /**
     * 최신순 게시글 조회를 담당하는 Service
     *
     * @version 0.1
     * @author 손승범
     * @Param page 현재 페이지
     * @Param size 게시물 갯수
     * */
    @Transactional(readOnly = true)
    public ReadList readAll(int page, int size) {
        Pageable pageable = createPageByCreatedAt(page,size);
        Page<ReviewPost> postList = reviewRepository.findAll(pageable);
        return entityToDtoByReadAll(postList);
    }

    /**
     * 좋아요순, 조회순 게시글 정렬을 담당하는 Service
     *
     * @version 0.1
     * @author 손승범
     * @Param type 정렬 태그
     * @Param page 현재 페이지
     * @Param size 게시물 갯수
     * */
    //좋아요순, 조회순 구현
    public ReadList readByFilter(String type, Integer page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewPost> postList = reviewPostCustomRepository.findAllByType(type, pageable);
        return entityToDtoByReadAll(postList);
    }

    private ReadList entityToDtoByReadAll(Page<ReviewPost> entity) {
        int totalPages =  entity.getTotalPages();
        int currentPage = entity.getNumber();
        List<ReviewPostAllDto> dtoList = entity.getContent()
                .stream()
                .map(reviewPost -> new ReviewPostAllDto(reviewPost))
                .collect(Collectors.toList());
        return ReadList.builder()
                .list(dtoList)
                .totalPages(totalPages)
                .currentPage(currentPage)
                .build();
    }

    /**
     * 게시글 검색을 담당하는 Service
     *
     * @version 0.1
     * @author 손승범
     * @Param type 검색어 종류(작성자. 게시글, 본문)
     * @Param keyword 검색어
     * @Param page 현재 페이지
     * @Param size 게시물 갯수
     * */
    //좋아요
    @Transactional(readOnly = true)
    public ReadList readByKeyword(String type, Integer page, int size, String keyword) {
        Pageable pageable = createPageByCreatedAt(page,size);
        Page<ReviewPost> postList = reviewPostCustomRepository.findAllByKeyword(type, keyword,pageable);
        return entityToDtoByReadAll(postList);
    }
    private List<ReviewIndexResponse> entityToDtoByIndex(List<ReviewPost> reviewPostList) {

        List<ReviewIndexResponse> dtoList = reviewPostList
                .stream()
                .map(reviewPost -> new ReviewIndexResponse(reviewPost))
                .collect(Collectors.toList());
        return dtoList;
    }
    /**
     * 단일 게시글 이동을 담당하는 Service
     *
     * @version 0.1
     * @author 손승범
     * @Param reviewPostId 게시글의 아이디
     * */
    public ReadOneReviewDto readOne(Long reviewPostId) {
        Optional<ReviewPost> reviewPost = reviewRepository.findByIdWithMemberAndImage(reviewPostId);
        ReviewPost reviewEntity = checkOptional(reviewPost);
        reviewEntity.increaseViewCount();
        return readOneEntityToDto(reviewEntity);
    }

    private ReviewPost checkOptional(Optional<ReviewPost>reviewPost){
        return reviewPost.orElseThrow(()-> new NotFoundException("게시물 상세보기 -> 해당 게시물이 존재하지 않습니다."));
    }
    private ReadOneReviewDto readOneEntityToDto(ReviewPost reviewPost
    ){
        ReadOneReviewDto readOneReviewDto = new ReadOneReviewDto(reviewPost);
        return readOneReviewDto;
    }

    public List<ReviewIndexResponse> readByLike(){
        List<ReviewPost> reviewPostList = reviewPostCustomRepository.findPostByLike();
        return entityToDtoByIndex(reviewPostList);
    }

    /**
     * 게시글 업데이트를 담당하는 Service
     *
     * @version 0.1
     * @author 손승범
     * @Param updatePostDto 수정하고자 하는 내용이 담긴 DTO
     * @Param reviewPostId 게시글 아이디
     * */
    public ReviewPost updateReviewPost(CreateReviewPostDto updatePostDto, Long reviewPostId) {
        dtoCheck(updatePostDto);
        ReviewPost reviewPost = findReviewPostCheckOptional(reviewPostId);
        reviewPost.update(updatePostDto);
        return reviewPost;
    }
    //
    private ReviewPost findReviewPostCheckOptional(Long reviewPostId){
        Optional<ReviewPost> reviewPostOptional = reviewRepository.findById(reviewPostId);
        return postCheckOptional(reviewPostOptional, reviewPostId);
    }
    private void dtoCheck(CreateReviewPostDto updatePostDto) {
        String title = updatePostDto.getTitle();
        String content = updatePostDto.getContent();
        if (title==null || content ==null){
            throw new ReviewDtoNullException("필수 입력 값을 입력하지 않았습니다. 제목은 필수 입니다. 입력 받은 제목: "+title+ "입력 받은 내용: "+content);
        }
    }
    public void delete(Long reviewPostId) {
        ReviewPost reviewPost = findReviewPostCheckOptional(reviewPostId);
        reviewPost.changeStatus();
    }
    private ReviewPost postCheckOptional(Optional<ReviewPost> reviewPost, Long reviewPostId){
        return reviewPost.orElseThrow(
                ()-> new NotFoundException("해당 게시글의 id가 유효하지 않습니다 유효하지 않은 reviewPostId: "+reviewPostId));
    }
    public ReviewPost findById(Long reviewPostId){
        Optional<ReviewPost> reviewPost = reviewRepository.findById(reviewPostId);
        return postCheckOptional(reviewPost, reviewPostId);
    }
    private Member findMemberById(MemberDto memberDto){
        Long memberDtoId = memberDto.getId();
        Optional<Member> optionalMember = memberRepository.findById(memberDtoId);
        return optionalMember.orElseThrow(()->new NotFoundException("해당 게시글의 유효 id가 유효하지 않습니다. 유효하지 않은 memberId: "+ memberDtoId));
    }

}
