package com.project.animal.adoption.controller;

import com.project.animal.adoption.domain.Adoption;
import com.project.animal.adoption.domain.AdoptionImage;
import com.project.animal.adoption.dto.AdoptionReadDto;
import com.project.animal.adoption.dto.AdoptionWriteDto;
import com.project.animal.adoption.repository.AdoptionRepository;
import com.project.animal.adoption.service.AdoptionImageService;
import com.project.animal.adoption.service.AdoptionService;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdoptionController {

    private final AdoptionService adoptionService;
    private final AdoptionImageService adoptionImageService;
    private final AdoptionRepository adoptionRepository;


    //리스트 들어오기
    @GetMapping("/v1/adoption")
    public String adoptionMain(Model model){

        List<Adoption> foundAdoptionList = adoptionService.findAll();
        model.addAttribute("list",foundAdoptionList);

        return "adoption/adoption_list";
    }


    // 글 쓰기 들어오기
    @GetMapping("/v1/adoption/edit")
    public String adoptionWrite(){

        return "adoption/adoption_write";
    }


    //  글쓰기 쓰고 post 보내는 영역
    @PostMapping("/v1/adoption/edit")
    public String adoptionWritePost(@Validated @ModelAttribute AdoptionWriteDto adoptionWriteDto, BindingResult bindingResult, @RequestParam(name="image") MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        if(bindingResult.hasErrors()){
            log.info("errors2222={}", bindingResult);

        }

        adoptionService.save(adoptionWriteDto, file);

        return "/adoption/adoption_list";
//        return "redirect: /v1/adoption";
    }

    // 수정 영역
    @GetMapping("/v1/adoption/edit/{id}")
    public String adoptionEdit(){

        return "redirect: /adoption/adoption_edit";
    }
    
    
    // 읽기 영역
    @GetMapping("/v1/adoption/{id}")
    public String adoptionRead(@PathVariable Long id, Model model){
        // 조회수 올리기
//        adoptionService.plusView(id);

        // id를 사용하여 DB에서 해당 게시물 정보를 조회
//        Optional<Adoption> foundAdoption = adoptionService.findById(id);
//
//        if(foundAdoption.isPresent()){
//            Adoption adoption = foundAdoption.get();
//            AdoptionReadDto adoptionReadDto = new AdoptionReadDto(adoption);
//            System.out.println("path 찾기"+adoption.getAdoptionImage().getPath());
//            System.out.println("path 찾기"+adoptionReadDto.getPath());
//            model.addAttribute("read",adoptionReadDto);
//        }
         Adoption adoption = adoptionRepository.findByIdWithImage(id);
         AdoptionReadDto adoptionReadDto = new AdoptionReadDto(adoption);
         model.addAttribute("read", adoptionReadDto);

//        Optional<AdoptionImage> foundImage = adoptionImageService.findById(postId);
//        AdoptionImage adoptionImage = foundImage.get();
//        model.addAttribute("image",adoptionImage);
//
//        System.out.println("foundImage"+foundImage);
//        // 조회한 게시물 정보를 모델에 추가
//        foundAdoption.ifPresent(adoption -> {
//            model.addAttribute("adoption", adoption);
//        });
//        foundImage.ifPresent(image -> {
//            model.addAttribute("image",adoptionImage);
//        });



        return "adoption/adoption_read";
    }

}
