package com.project.animal.adoption.controller;

import com.project.animal.adoption.constant.ViewName;
import com.project.animal.adoption.domain.Adoption;
import com.project.animal.adoption.domain.AdoptionComment;
import com.project.animal.adoption.domain.AdoptionImage;
import com.project.animal.adoption.dto.*;
import com.project.animal.adoption.repository.AdoptionRepository;
import com.project.animal.adoption.service.AdoptionCommentServiceImpl;
import com.project.animal.adoption.service.AdoptionServiceImpl;
import com.project.animal.global.common.annotation.Member;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdoptionController {

    private final AdoptionServiceImpl adoptionService;
    private final AdoptionRepository adoptionRepository;
    private final AdoptionCommentServiceImpl adoptionCommentService;
    private final MemberRepository repository;


    //멤버 정보 불러올 공통 영역
    @ModelAttribute("member")
    public MemberDto addMemberInModel(@Member MemberDto member) {
        return member;
    }


    // 메인 리스트 입장
    @GetMapping("/v1/adoption")
    public String adoptionMainGet(@RequestParam(required = false) Integer pageNumber,
                                  @RequestParam(required = false) String breed,
                                  Model model,
                                  @Member MemberDto memberDto){

        int pageSize = 10; // 한 페이지에 보여줄 데이터 개수

        if (pageNumber == null || pageNumber == 0) {
            pageNumber = 1; // 페이지 번호가 없거나 1보다 작으면 기본값으로 1 설정
        }

        // index페이지에서 넘어오는 강아지, 고양이 리스트 불러오기
        Page<Adoption> list = null;
        if(breed != null){
            if(breed.equals("개") || breed.equals("고양이")){
                list = adoptionService.findPatsByBreed(breed, pageNumber, pageSize);
            }
        }else{
            // 고양이 , 강아지 종으로 넘어오는 경우가 아닌 모든 경우
            //전체 리스트
            list = adoptionService.getAdoptionPageWithImagesAndMemberPages(pageNumber,pageSize); // 전체 리스트
        }


        int count = list.getTotalPages(); // 총 불러온 리스트의 갯수
        int BLOCK_COUNT = 10; // 페이지 넘버 갯수
        int temp = (pageNumber-1)%BLOCK_COUNT; //  페이지 넘버 갯수에 맞게 시작페이지 보이도록 계산해주는 변수
        int startPage = pageNumber-temp; // 페이지네이션에서 보여줄 시작 페이지
        int endPage = startPage + BLOCK_COUNT -1; // 페이지네이션에서 보여줄 끝 페이지
//        int pageCount = (count / pageSize) + (count % pageSize == 0 ? 0 : 1); //



        model.addAttribute("list", list);
        model.addAttribute("blockCount", BLOCK_COUNT);
        model.addAttribute("startPage", startPage);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("endPage", endPage);
        model.addAttribute("count", count);
        model.addAttribute("breed", breed);
//        model.addAttribute("pageCount", pageCount);

        // 좋아요 리스트를 체크하기 위한 로직
        if(memberDto != null){
            List<Boolean> likeStatusList = new ArrayList<>();
            for (Adoption adoption : list.getContent()) {
                boolean liked = adoptionService.isAdoptionLikedByUser(adoption.getId(), memberDto.getId());
                likeStatusList.add(liked);
            }
            model.addAttribute("likeStatusList", likeStatusList);
        }


        return ViewName.ADOPTION_LIST;
    }


    // 입양 게시판 list 좋아요 처리 영역
    @PostMapping ("/v1/adoption")
    @ResponseBody
    public Map<String, Object> adoptionMainPost(@RequestBody Map<String, String> requestBody, @Member MemberDto memberDto){

        Long postId = Long.valueOf(requestBody.get("postId"));
        Map<String, Object> responseBody = new HashMap<>();

        Optional<Adoption> adoption = adoptionRepository.findById(postId);

        if(adoption.isEmpty()){
            System.out.println("이 게시판이 없습니다.");
            throw new RuntimeException("이 게시판이 없습니다.");
        }

        int likeCount = adoptionService.likeAdoption(postId, memberDto);
        boolean likedByUser = adoptionService.isAdoptionLikedByUser(postId, memberDto.getId());

        responseBody.put("status", likedByUser);
        responseBody.put("likeCount", likeCount);
        return responseBody;

    }


    // 글 쓰기 들어오기
    @GetMapping("/v1/adoption/edit")
    public String adoptionWrite(@Member MemberDto member){

        if(member == null ){
            return "redirect:/v1/adoption";
        }else{
            return ViewName.ADOPTION_WRITE;
        }
    }


    //  글쓰기 쓰고 post 보내는 영역
    @PostMapping("/v1/adoption/edit")
    public String adoptionWritePost(@ModelAttribute @Validated AdoptionWriteDto adoptionWriteDto, BindingResult bindingResult,
                                    @RequestParam(name="image") List<MultipartFile> file,
                                    @Member MemberDto member) {

        if(bindingResult.hasErrors()){
            log.info("adoption_write binding error = {}", bindingResult);

            return "redirect:/v1/adoption/edit";
        }

        adoptionService.save(adoptionWriteDto, file, member);

//        return "redirect:/v1/adoption";
        return "redirect:/v1/adoption";
    }

    // 글쓰기 영역 adoption_write
    @GetMapping("/v1/adoption/edit/{id}")
    public String adoptionEditGet(@PathVariable Long id, Model model){


        Adoption adoption = adoptionService.findByIdWidhImageAndMember(id);
        AdoptionEditDto adoptionEditDto = new AdoptionEditDto(adoption,id);

        model.addAttribute("edit", adoptionEditDto);

        return ViewName.ADOPTION_EDIT;
    }

    // 게시글 상세 읽기 영역
    @GetMapping("/v1/adoption/{id}")
    public String adoptionRead(@PathVariable(name="id") Long postId, Model model, @Member MemberDto memberDto){
        // 조회수 올리기
        adoptionService.plusView(postId);

        Adoption adoption = adoptionService.findByIdWidhImageAndMember(postId);

        AdoptionReadDto adoptionReadDto = new AdoptionReadDto(adoption, postId, memberDto);
        model.addAttribute("read", adoptionReadDto);

        List<AdoptionComment> allComment = adoptionCommentService.findTopLevelCommentsByAdoptionId(postId);
        model.addAttribute("comments", allComment);

        return ViewName.ADOPTION_READ;
    }


    // 상세 게시글 수정 하고 Post 보내는 영역
    @PostMapping ("/v1/adoption/edit/{id}")
    public String adoptionEditPut(@ModelAttribute @Validated AdoptionEditDto adoptionEditDto, BindingResult bindingResult,
                                  @RequestParam(name="image") List<MultipartFile> file,
                                  @PathVariable(name="id") Long postId,
                                  @Member MemberDto memberDto){

        if(bindingResult.hasErrors()){
            log.info("adoption_edit binding error = {}", bindingResult);
            return "redirect:/v1/adoption/edit/{id}";
//            return EndPoint.ADOPTION_LIST;
        }

        // 로그인한 아이디와 글쓴 아이디가 같으면 수정
        if(adoptionEditDto.getAuthorId() == memberDto.getId()){
            adoptionService.update(adoptionEditDto, file, postId);
        }

        return "redirect:/v1/adoption";
//        return EndPoint.ADOPTION_LIST;
    }


    // 게시글 상세 수정 영역 중 게시글 삭제, 이미지 삭제 영역
    @CrossOrigin(origins = {"http://localhost:8080", "https://infra.shucloud.site", "toyproject.shucloud.site", "https://toyproject.shucloud.site"})
    @DeleteMapping("/v1/adoption/edit/{id}")
    @ResponseBody
    public ResponseEntity<String> handleImageDeleteRequest(@RequestBody Map<String, String> requestBody, @PathVariable Long id) {

        try{
            // 이미지, 게시글 삭제 로직
            Adoption adoption = adoptionService.findByIdWidhImageAndMember(id);

            // deleteImageId를 사용하여 이미지 삭제 (status 0으로 변경)
            if(requestBody.get("deleteImageIndex") != null) {
                String deleteImageIndex = requestBody.get("deleteImageIndex");

                adoption.getAdoptionImages().get(Integer.parseInt(deleteImageIndex)).changeIsActive(0);
                adoptionService.simpleSave(adoption);
            }else if (requestBody.get("deletePostId") != null){
                // deletePostId 사용하여 게시글 삭제 (status 0으로 변경)

                adoption.changeIsActive(0);
                for (AdoptionImage adoptionImage : adoption.getAdoptionImages()) {
                    adoptionImage.changeIsActive(0);
                }
                adoptionService.simpleSave(adoption);

                return ResponseEntity.ok().body("/v1/adoption");
            }else{
                log.info("입양 게시판 이미지 및 게시글 삭제 둘다 안된 경우");
            }
        }catch (Exception e){
            new RuntimeException(e);
            System.out.println("이미지 삭제 및 게시글 삭제 에러" + e.getMessage());
        }
        // 삭제 결과에 따라 적절한 응답 반환
        return ResponseEntity.ok("success delete");
    }





    // 댓글 쓰기 (읽기영역 내)
    @PostMapping("/v1/adoption/{id}")
    public String adoptionCommentPost(@ModelAttribute @Validated AdoptionCommentWriteDto adoptionCommentWriteDto, BindingResult bindingResult,
                                      @PathVariable(name = "id") Long postId,
                                      @RequestParam(name="commentId", required = false) Long commentId,
                                      @Member MemberDto memberDto){

        if(bindingResult.hasErrors()){
            log.info("adoption_read 영역 내 댓글 에러 ={}", bindingResult);

            return "redirect:/v1/adoption/{id}";
        }

        if (commentId != null) {
            // 기존 댓글 업데이트
            adoptionCommentService.updateComment(adoptionCommentWriteDto, commentId);
        } else {
            // 새로운 댓글 생성
            adoptionCommentService.saveComment(adoptionCommentWriteDto, postId, memberDto);
        }

        return "redirect:/v1/adoption/{id}";
    }

    // 댓글 / 대댓글 삭제
    @CrossOrigin(origins = {"http://localhost:8080", "http://infra.shucloud.site"})
    @DeleteMapping("/v1/adoption/{id}")
    @ResponseBody
    public ResponseEntity<String> adoptionCommentDelete(@RequestBody Map<String, String> requestBody, @PathVariable(name="id") Long postId){


        try {
            if(requestBody.get("deleteCommentParentIndex") != null) {
                Long deleteCommentIndex = Long.valueOf(requestBody.get("deleteCommentParentIndex"));

                // 댓글과 대댓글 함께 삭제 로직 수행
                adoptionCommentService.deleteParentAndChildComment(deleteCommentIndex);

            }else if(requestBody.get("deleteCommentChildIndex") != null){
                Long deleteCommentChildIndex = Long.valueOf(requestBody.get("deleteCommentChildIndex"));
                // 대댓글만 삭제 로직 수행
                adoptionCommentService.deleteChildComment(deleteCommentChildIndex);
            }
            return ResponseEntity.ok("/v1/adoption/{id}");
        } catch (Exception e) {
            log.error("댓글 삭제 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail Comment delete");
        }
//        return ResponseEntity.ok( EndPoint.ADOPTION_READ);
    }


}


