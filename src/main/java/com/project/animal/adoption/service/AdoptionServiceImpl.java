package com.project.animal.adoption.service;

import com.project.animal.adoption.domain.Adoption;
import com.project.animal.adoption.domain.AdoptionImage;
import com.project.animal.adoption.dto.AdoptionEditDto;
import com.project.animal.adoption.dto.AdoptionWriteDto;
import com.project.animal.adoption.repository.AdoptionImageRepository;
import com.project.animal.adoption.repository.AdoptionRepository;
import com.project.animal.adoption.service.inf.AdoptionService;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.member.exception.LoginException;
import com.project.animal.sample.openApi.dto.OpenApiDto;
import com.project.animal.member.domain.Member;
import com.project.animal.member.repository.MemberRepository;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.ServerException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class AdoptionServiceImpl implements AdoptionService {


    private final AdoptionRepository adoptionRepository;
    private final AdoptionImageRepository adoptionImageRepository;
    private final MinioClient minioClient;
    private final MemberRepository repository;



    public void save(AdoptionWriteDto adoptionWriteDto, List<MultipartFile> files, MemberDto memberDto) {
        Member member = repository.findById(memberDto.getId()).orElseThrow(LoginException :: new);


        String serverFileName;
        Adoption adoption = new Adoption(adoptionWriteDto.getTitle(), adoptionWriteDto.getBreed(), adoptionWriteDto.getGender(), adoptionWriteDto.getAge(), adoptionWriteDto.getCenter(), adoptionWriteDto.getNeutered(), adoptionWriteDto.getContent(), adoptionWriteDto.getColor(), adoptionWriteDto.getHappenPlace(), adoptionWriteDto.getSpecialMark(), member);

        Adoption saved = adoptionRepository.save(adoption);

        for (MultipartFile file : files) {

            serverFileName = saveMinio(file); // 미니오에 저장하는 영역
            AdoptionImage adoptionImage = new AdoptionImage(serverFileName, saved);

            adoptionImageRepository.save(adoptionImage);
        }


   }




   //추후 minio 공통화 작업 후 삭제 예정
    private String saveMinio(MultipartFile file) {
      String serverFileName;
        try {
            if (file.isEmpty()) {
                return "empty";
            } else {

                serverFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                InputStream is = file.getInputStream();

                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket("adoption")
                                .object(serverFileName)
                                .contentType(file.getContentType())
                                .stream(is, is.available(), 0)
                                .build()
                );
                is.close();
                return serverFileName;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void simpleSave(Adoption adoption){
        adoptionRepository.save(adoption);
    }


    @Transactional
    public void update (AdoptionEditDto adoptionEditDto, List<MultipartFile> files, Long postId) {
        String serverFileName;
        Adoption adoption = adoptionRepository.findByIdWithImageAndMember(postId);

        adoption.updateAdoption(adoptionEditDto);

        Adoption saved = adoptionRepository.save(adoption);

            for (MultipartFile file : files) {
                if(file.getSize()==0){
                    System.out.println("입양 상세 페이지 수정 중 첨부된 file이 없습니다.");
                }else {
                    serverFileName = saveMinio(file); // 미니오에 저장하는 영역
                    AdoptionImage adoptionImage = new AdoptionImage(serverFileName, saved);
//                AdoptionImage adoptionImage = new AdoptionImage();
//                adoptionImage.changeImage(serverFileName, adoption);
                    adoptionImageRepository.save(adoptionImage);
                }


        }

    }


    public Optional<Adoption> findById(Long id) {

        Optional<Adoption> foundAdoptionId = adoptionRepository.findById(id);
        if (foundAdoptionId.isPresent()) {
            Adoption adoption = foundAdoptionId.get();

            plusView(id);
            adoptionRepository.save(adoption);

        }

        return foundAdoptionId;
    }


    public List<Adoption> findAll() {
       return adoptionRepository.findAll();
    }

    public int getCount(List<Adoption> list){
        return list.size();
    }

    public Page<Adoption> getAdoptionPageWithImagesAndMember(int pageNumber, int pageSize) {
        // 페이징 정보 생성
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        // adoption_image와 member를 함께 가져오는 메소드 호출
        List<Adoption> adoptionList = adoptionRepository.findAllWithImagesAndMember();

        // 가져온 리스트를 페이징 처리하여 반환
        return new PageImpl<>(adoptionList, pageable, adoptionList.size());
    }

    public Page<Adoption> getAdoptionPageWithImagesAndMemberPages(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return adoptionRepository.findAllWithImagesAndMemberPages(pageable);
    }

    public void plusView(Long id){
        Optional<Adoption> foundAdoptionId = adoptionRepository.findById(id);
        Adoption adoption = foundAdoptionId.get();

        int hit = adoption.getHit();
        hit++;
        adoption.setHit(hit);

        adoptionRepository.save(adoption);

    }

    public void apiSave(List<OpenApiDto> openApiDtoList) {
        // 센터에서 받은 api를 담는 영역
        log.info("no1: >> " + openApiDtoList.size());

        for (OpenApiDto openApiDto : openApiDtoList) {

            Adoption adoption = new Adoption(openApiDto.getDesertionNo(), openApiDto.getKindCd(),openApiDto.getColorCd(),openApiDto.getAge(),openApiDto.getNoticeSdt(),openApiDto.getNoticeEdt(),openApiDto.getProcessState(),openApiDto.getSexCd(),openApiDto.getNeuterYn(), openApiDto.getCareAddr(),openApiDto.getCareNm(),openApiDto.getNoticeNo(),openApiDto.getSpecialMark(),openApiDto.getHappenPlace());

            Optional<Member> member = repository.findById(1L);
            Member member1 = member.get();
            adoption.setMember(member1);

            Adoption saved = adoptionRepository.save(adoption);


            AdoptionImage adoptionImage = new AdoptionImage(openApiDto.getPopfile(), saved);

            adoptionImageRepository.save(adoptionImage);


        }
    }

    public Adoption findByIdWithImage(Long id){
      return adoptionRepository.findByIdWithImage(id);
    }

    public Adoption findByIdWidhImageAndMember(Long id){
        return adoptionRepository.findByIdWithImageAndMember(id);
    }


    public List<Adoption> findAllWithImages() {
        return adoptionRepository.findAllWithImages();
    }

    public List<Adoption> findAllWithImagesAndMember() {
        return adoptionRepository.findAllWithImagesAndMember();

    }


}
