package com.project.animal.adoption.controller;

import com.project.animal.adoption.domain.Adoption;
import com.project.animal.adoption.domain.AdoptionComment;
import com.project.animal.adoption.domain.AdoptionImage;
import com.project.animal.adoption.dto.*;
import com.project.animal.adoption.service.AdoptionCommentServiceImpl;
import com.project.animal.adoption.service.AdoptionServiceImpl;
import com.project.animal.global.common.constant.EndPoint;
import com.project.animal.global.common.constant.ViewName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdoptionController {

    private final AdoptionServiceImpl adoptionService;
    private final AdoptionCommentServiceImpl adoptionCommentService;

    // 메인 리스트 입장
    @GetMapping(EndPoint.ADOPTION_LIST)
    public String adoptionMain(Model model){
        List<Adoption> allWithImages = adoptionService.findAllWithImagesAndMember();

        model.addAttribute("list",allWithImages);

        return ViewName.ADOPTION_LIST;
    }


    // 글 쓰기 들어오기
    @GetMapping(EndPoint.ADOPTION_WRITE)
    public String adoptionWrite(){

        return ViewName.ADOPTION_WRITE;
    }


    //  글쓰기 쓰고 post 보내는 영역
    @PostMapping(EndPoint.ADOPTION_WRITE)
    public String adoptionWritePost(@ModelAttribute @Validated AdoptionWriteDto adoptionWriteDto, BindingResult bindingResult,
                                    @RequestParam(name="image") List<MultipartFile> file) {

        if(bindingResult.hasErrors()){
            log.info("adoption_write binding error = {}", bindingResult);

            return "redirect:"+EndPoint.ADOPTION_WRITE;
        }

        adoptionService.save(adoptionWriteDto, file);

//        return "redirect:/v1/adoption";
        return "redirect:"+EndPoint.ADOPTION_LIST;
    }

    // 수정 하기 위해 들어오는 쓰기 영역
    @GetMapping(EndPoint.ADOPTION_EDIT)
    public String adoptionEditGet(@PathVariable Long id, Model model){


        Adoption adoption = adoptionService.findByIdWidhImageAndMember(id);
        AdoptionEditDto adoptionEditDto = new AdoptionEditDto(adoption,id);

        model.addAttribute("edit", adoptionEditDto);

        return ViewName.ADOPTION_EDIT;
    }


    // 수정 하고 PUT 보내는 영역
    @PostMapping (EndPoint.ADOPTION_EDIT)
    public String adoptionEditPut(@ModelAttribute @Validated AdoptionEditDto adoptionEditDto, BindingResult bindingResult,
                               @RequestParam(name="image") List<MultipartFile> file,
                                  @PathVariable Long id){

        if(bindingResult.hasErrors()){
            log.info("adoption_edit binding error = {}", bindingResult);
            return "redirect:"+EndPoint.ADOPTION_EDIT;
//            return EndPoint.ADOPTION_LIST;
        }

        adoptionService.update(adoptionEditDto, file, id);


        return "redirect:"+EndPoint.ADOPTION_LIST;
//        return EndPoint.ADOPTION_LIST;
    }


    @CrossOrigin(origins = {"http://localhost:8080", "http://infra.shucloud.site"})
    @PutMapping(EndPoint.ADOPTION_EDIT )
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
    
    
    // 게시글 상세 읽기 영역
    @GetMapping(EndPoint.ADOPTION_READ)
    public String adoptionRead(@PathVariable Long id, Model model){
        // 조회수 올리기
        adoptionService.plusView(id);

         Adoption adoption = adoptionService.findByIdWidhImageAndMember(id);
         AdoptionReadDto adoptionReadDto = new AdoptionReadDto(adoption, id);
         model.addAttribute("read", adoptionReadDto);

        List<AdoptionComment> allComment = adoptionCommentService.findTopLevelCommentsByAdoptionId(id);
        model.addAttribute("comments", allComment);

        return ViewName.ADOPTION_READ;
    }


    // 댓글 쓰기 (읽기영역 내)
    @PostMapping(EndPoint.ADOPTION_READ)
    public String adoptionCommentPost(@ModelAttribute @Validated AdoptionCommentWriteDto adoptionCommentWriteDto, BindingResult bindingResult,
                                      @PathVariable(name = "id") Long postId ){

        if(bindingResult.hasErrors()){
            log.info("adoption_read 영역 내 댓글 에러 ={}", bindingResult);

            return "redirect:"+EndPoint.ADOPTION_READ;
        }

        System.out.println("comment check: >>"+adoptionCommentWriteDto.toString());
        System.out.println("comment check: id>>"+adoptionCommentWriteDto.getId());
        System.out.println("comment check: author>>"+adoptionCommentWriteDto.getAuthor());
        System.out.println("comment check: content>>"+adoptionCommentWriteDto.getContent());

        adoptionCommentService.saveComment(adoptionCommentWriteDto, postId);

        return "redirect:"+EndPoint.ADOPTION_READ;


    }

}


