package com.project.animal.review.service;

import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.member.domain.Member;
import com.project.animal.member.repository.MemberRepository;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.dto.*;
import com.project.animal.review.exception.NotFoundException;
import com.project.animal.review.exception.ReviewDtoNullException;
import com.project.animal.review.repository.ReviewRepository;
import io.minio.MinioClient;
import lombok.AllArgsConstructor;
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
    @Transactional(readOnly = true)
    public ReadListGeneric readAll(int page, int size) {
        Pageable pageable = createPageByCreatedAt(page,size);
        Page<ReviewPost> postList = reviewRepository.findAll(pageable);

        return entityToDtoByReadAll(postList);
    }
    private ReadListGeneric entityToDtoByReadAll(Page<ReviewPost> entity) {
        int totalPages =  entity.getTotalPages();
        int currentPage = entity.getNumber();
        List<ReviewPostAllDto> dtoList = entity.getContent()
                .stream()
                .map(reviewPost -> new ReviewPostAllDto(reviewPost))
                .collect(Collectors.toList());
        return ReadListGeneric.builder()
                .list(dtoList)
                .totalPages(totalPages)
                .currentPage(currentPage)
                .build();
    }
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
    @Transactional(readOnly = true)
    private ReadListGeneric readByName(Integer page, int size, String nickname) {
        Pageable pageable = createPageByCreatedAt(page,size);
        Page<ReviewPost> postList = reviewRepository.findAllWithMemberAndImageByNickname(nickname,pageable);
        return entityToDtoByReadAll(postList);
    }
    public ReadListGeneric<ReadListGeneric> readBySearch(String type, String keyword, Integer page, int size) {
        switch (type){
            case "author":
                return readByName(page ,size, keyword);
            case "title":
                return readByTitle(page,size,keyword);
            case "content":
                return readByContent(page, size, keyword);
        }
        return null;
    }
    @Transactional(readOnly = true)
    private ReadListGeneric<ReadListGeneric> readByContent(Integer page, int size, String content) {
        Pageable pageable = createPageByCreatedAt(page,size);
        Page<ReviewPost> postList = reviewRepository.findAllWithMemberAndImageByContent(content, pageable);
        return entityToDtoByReadAll(postList);
    }
    @Transactional(readOnly = true)
    private ReadListGeneric<ReadListGeneric> readByTitle(Integer page, int size, String title) {
        Pageable pageable = createPageByCreatedAt(page,size);
        Page<ReviewPost> postList = reviewRepository.findAllWithMemberAndImageByTitle(title,pageable);
        return entityToDtoByReadAll(postList);
    }

    public ReviewPost updateReviewPost( CreateReviewPostDto updatePostDto, Long reviewPostId) {
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
