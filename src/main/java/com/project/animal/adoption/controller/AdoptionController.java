package com.project.animal.adoption.controller;

import com.project.animal.adoption.domain.Adoption;
import com.project.animal.adoption.dto.AdoptionReadDto;
import com.project.animal.adoption.dto.AdoptionWriteDto;
import com.project.animal.adoption.service.AdoptionService;
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

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdoptionController {

    private final AdoptionService adoptionService;

    //리스트 들어오기
    @GetMapping("/v1/adoption")
    public String adoptionMain(Model model){

//        List<Adoption> foundAdoptionList = adoptionService.findAll();
        List<Adoption> allWithImages = adoptionService.findAllWithImagesAndMember();


        model.addAttribute("list",allWithImages);

        return "adoption/adoption_list";
    }


    // 글 쓰기 들어오기
    @GetMapping("/v1/adoption/edit")
    public String adoptionWrite(){

        return "adoption/adoption_write";
    }


    //  글쓰기 쓰고 post 보내는 영역
    @PostMapping("/v1/adoption/edit")
    public String adoptionWritePost(@ModelAttribute @Validated AdoptionWriteDto adoptionWriteDto, BindingResult bindingResult,
                                    @RequestParam(name="image") List<MultipartFile> file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        if(bindingResult.hasErrors()){
            log.info("errors2222={}", bindingResult);

        }

        System.out.println("multipart: controller>>"+file);
        System.out.println("multipart: controller>>"+file.size());

        adoptionService.save(adoptionWriteDto, file);

//        return "/adoption/adoption_list";
        return "redirect:/v1/adoption";
    }

    // 수정 영역
    @GetMapping("/v1/adoption/edit/{id}")
    public String adoptionEdit(){

        return "redirect:/adoption/adoption_edit";
    }
    
    
    // 읽기 영역
    @GetMapping("/v1/adoption/{id}")
    public String adoptionRead(@PathVariable Long id, Model model){
        // 조회수 올리기
        adoptionService.plusView(id);

         Adoption adoption = adoptionService.findByIdWithImage(id);
         AdoptionReadDto adoptionReadDto = new AdoptionReadDto(adoption);
         model.addAttribute("read", adoptionReadDto);

        return "adoption/adoption_read";
    }

}


