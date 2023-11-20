package com.project.animal.adoption.controller;

import com.project.animal.adoption.domain.Adoption;
import com.project.animal.adoption.domain.AdoptionComment;
import com.project.animal.adoption.domain.AdoptionImage;
import com.project.animal.adoption.domain.AdoptionPostLike;
import com.project.animal.adoption.dto.*;
import com.project.animal.adoption.service.AdoptionCommentServiceImpl;
import com.project.animal.adoption.service.AdoptionServiceImpl;
import com.project.animal.global.common.annotation.Member;
import com.project.animal.global.common.constant.EndPoint;
import com.project.animal.global.common.constant.ViewName;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdoptionController {

    private final AdoptionServiceImpl adoptionService;
    private final AdoptionCommentServiceImpl adoptionCommentService;
    private final MemberRepository repository;


    //멤버 정보 불러올 공통 영역
    @ModelAttribute("member")
    public MemberDto addMemberInModel(@Member MemberDto member) {
        return member;
    }


    // 메인 리스트 입장
    @GetMapping(EndPoint.ADOPTION_LIST)
    public String adoptionMainGet(@RequestParam(required = false) Integer pageNumber, Model model){
        int pageSize = 10; // 한 페이지에 보여줄 데이터 개수

        if (pageNumber == null || pageNumber == 0) {
            pageNumber = 1; // 페이지 번호가 없거나 1보다 작으면 기본값으로 1 설정
        }
        Page<Adoption> listWithImagesAndMember = adoptionService.getAdoptionPageWithImagesAndMemberPages(pageNumber,pageSize); // 전체 리스트
        int count = listWithImagesAndMember.getTotalPages(); // 총 불러온 리스트의 갯수
        int BLOCK_COUNT = 10; // 페이지 넘버 갯수
        int temp = (pageNumber-1)%BLOCK_COUNT; //  페이지 넘버 갯수에 맞게 시작페이지 보이도록 계산해주는 변수
        int startPage = pageNumber-temp; // 페이지네이션에서 보여줄 시작 페이지
        int endPage = startPage + BLOCK_COUNT -1; // 페이지네이션에서 보여줄 끝 페이지
//        int pageCount = (count / pageSize) + (count % pageSize == 0 ? 0 : 1); //


        model.addAttribute("list", listWithImagesAndMember);
        model.addAttribute("blockCount", BLOCK_COUNT);
        model.addAttribute("startPage", startPage);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("endPage", endPage);
        model.addAttribute("count", count);
//        model.addAttribute("pageCount", pageCount);

        List<AdoptionPostLike> adoptionPostLikes = adoptionService.adoptionPostLikesAll();

        model.addAttribute("postLikes",adoptionPostLikes);

        return ViewName.ADOPTION_LIST;
    }

    // 입양 게시판 list 좋아요 처리 영역
    @PostMapping (EndPoint.ADOPTION_LIST)
    @ResponseBody
    public Map<String, Object> adoptionMainPost(@RequestBody Map<String, String> requestBody, @Member MemberDto memberDto){

        Long postId = Long.valueOf(requestBody.get("postId"));
        Map<String, Object> responseBody = new HashMap<>();
        Adoption adoption = adoptionService.findById(postId).orElseThrow();
        AdoptionPostLike postLikeByAdoptionId = adoptionService.getPostLikeByAdoptionId(postId);
//        List<AdoptionPostLike> likesByAdoptionAndStatus = adoptionService.getLikesByAdoptionAndStatus(postId, 1);

        if(postLikeByAdoptionId == null){
            postLikeByAdoptionId = new AdoptionPostLike(adoption, repository.findById(memberDto.getId()).orElseThrow());

            adoptionService.adoptionPostListSave(postLikeByAdoptionId);
            adoptionService.simpleSave(adoption);
//            return responseBody;
        } else if(postLikeByAdoptionId != null){
            System.out.println("data:>>"+postLikeByAdoptionId.getStatus());
            // 상태값 변경
            if(postLikeByAdoptionId.getStatus()==0){
                postLikeByAdoptionId.setStatus(1);
            }else if (postLikeByAdoptionId.getStatus()==1){
                postLikeByAdoptionId.setStatus(0);
                // AdoptionPostLike 삭제
                adoption.getAdoptionPostLikes().remove(postLikeByAdoptionId);
            }

            postLikeByAdoptionId.setAdoption(adoption);
            postLikeByAdoptionId.setMember(adoption.getMember());
            // 저장
            adoptionService.simpleSave(adoption);
            adoptionService.adoptionPostListSave(postLikeByAdoptionId);

        }
        responseBody.put("status", postLikeByAdoptionId.getStatus());
        responseBody.put("likeCount", adoption.getAdoptionPostLikes().size());
        return responseBody;

    }


    // 글 쓰기 들어오기
    @GetMapping(EndPoint.ADOPTION_WRITE)
    public String adoptionWrite(@Member MemberDto member){

        if(member == null ){
            return "redirect:"+EndPoint.ADOPTION_LIST;
        }else{
            return ViewName.ADOPTION_WRITE;
        }
    }


    //  글쓰기 쓰고 post 보내는 영역
    @PostMapping(EndPoint.ADOPTION_WRITE)
    public String adoptionWritePost(@ModelAttribute @Validated AdoptionWriteDto adoptionWriteDto, BindingResult bindingResult,
                                    @RequestParam(name="image") List<MultipartFile> file,
                                    @Member MemberDto member) {

        if(bindingResult.hasErrors()){
            log.info("adoption_write binding error = {}", bindingResult);

            return "redirect:"+EndPoint.ADOPTION_WRITE;
        }

        adoptionService.save(adoptionWriteDto, file, member);

//        return "redirect:/v1/adoption";
        return "redirect:"+EndPoint.ADOPTION_LIST;
    }

    // 글쓰기 영역 adoption_write
    @GetMapping(EndPoint.ADOPTION_EDIT)
    public String adoptionEditGet(@PathVariable Long id, Model model){


        Adoption adoption = adoptionService.findByIdWidhImageAndMember(id);
        AdoptionEditDto adoptionEditDto = new AdoptionEditDto(adoption,id);

        model.addAttribute("edit", adoptionEditDto);

        return ViewName.ADOPTION_EDIT;
    }

    // 게시글 상세 읽기 영역
    @GetMapping(EndPoint.ADOPTION_READ)
    public String adoptionRead(@PathVariable(name="id") Long postId, Model model){
        // 조회수 올리기
        adoptionService.plusView(postId);

        Adoption adoption = adoptionService.findByIdWidhImageAndMember(postId);

        AdoptionReadDto adoptionReadDto = new AdoptionReadDto(adoption, postId);
        model.addAttribute("read", adoptionReadDto);

        List<AdoptionComment> allComment = adoptionCommentService.findTopLevelCommentsByAdoptionId(postId);
        model.addAttribute("comments", allComment);

        return ViewName.ADOPTION_READ;
    }


    // 상세 게시글 수정 하고 Post 보내는 영역
    @PostMapping (EndPoint.ADOPTION_EDIT)
    public String adoptionEditPut(@ModelAttribute @Validated AdoptionEditDto adoptionEditDto, BindingResult bindingResult,
                               @RequestParam(name="image") List<MultipartFile> file,
                                  @PathVariable(name="id") Long postId,
                                  @Member MemberDto memberDto){

        if(bindingResult.hasErrors()){
            log.info("adoption_edit binding error = {}", bindingResult);
            return "redirect:"+EndPoint.ADOPTION_EDIT;
//            return EndPoint.ADOPTION_LIST;
        }

        // 로그인한 아이디와 글쓴 아이디가 같으면 수정
        if(adoptionEditDto.getAuthorId() == memberDto.getId()){
            adoptionService.update(adoptionEditDto, file, postId);
        }

        return "redirect:"+EndPoint.ADOPTION_LIST;
//        return EndPoint.ADOPTION_LIST;
    }


    // 게시글 상세 수정 영역 중 게시글, 이미지 삭제 영역
    @CrossOrigin(origins = {"http://localhost:8080", "http://infra.shucloud.site"})
    @DeleteMapping(EndPoint.ADOPTION_EDIT )
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

                return ResponseEntity.ok().body(EndPoint.ADOPTION_LIST);
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
    @PostMapping(EndPoint.ADOPTION_READ)
    public String adoptionCommentPost(@ModelAttribute @Validated AdoptionCommentWriteDto adoptionCommentWriteDto, BindingResult bindingResult,
                                      @PathVariable(name = "id") Long postId,
                                      @RequestParam(name="commentId", required = false) Long commentId,
                                      @Member MemberDto memberDto){

        if(bindingResult.hasErrors()){
            log.info("adoption_read 영역 내 댓글 에러 ={}", bindingResult);

            return "redirect:"+EndPoint.ADOPTION_READ;
        }

        if (commentId != null) {
            // 기존 댓글 업데이트
            adoptionCommentService.updateComment(adoptionCommentWriteDto, commentId);
        } else {
            // 새로운 댓글 생성
            adoptionCommentService.saveComment(adoptionCommentWriteDto, postId, memberDto);
        }

        return "redirect:"+EndPoint.ADOPTION_READ;
    }

    // 댓글 / 대댓글 삭제
    @CrossOrigin(origins = {"http://localhost:8080", "http://infra.shucloud.site"})
    @DeleteMapping(EndPoint.ADOPTION_READ)
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
            return ResponseEntity.ok(EndPoint.ADOPTION_READ);
        } catch (Exception e) {
            log.error("댓글 삭제 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail Comment delete");
        }
//        return ResponseEntity.ok( EndPoint.ADOPTION_READ);
    }


}


