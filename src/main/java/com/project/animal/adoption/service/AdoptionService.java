package com.project.animal.adoption.service;

import com.project.animal.adoption.domain.Adoption;
import com.project.animal.adoption.domain.AdoptionImage;
import com.project.animal.adoption.dto.AdoptionWriteDto;
import com.project.animal.adoption.repository.AdoptionImageRepository;
import com.project.animal.adoption.repository.AdoptionRepository;
import com.project.animal.global.common.dto.OpenApiDto;
import com.project.animal.member.domain.Member;
import com.project.animal.member.repository.MemberRepository;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.ServerException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final AdoptionImageRepository adoptionImageRepository;
    private final MinioClient minioClient;
    private final MemberRepository repository;


    public void save(AdoptionWriteDto adoptionWriteDto, List<MultipartFile> files) {

        String serverFileName = null;
        Adoption adoption = new Adoption(adoptionWriteDto.getTitle(), adoptionWriteDto.getBreed(), adoptionWriteDto.getGender(), adoptionWriteDto.getAge(), adoptionWriteDto.getCenter(), adoptionWriteDto.getNeutered(), adoptionWriteDto.getContent(), adoptionWriteDto.getColor(), adoptionWriteDto.getHappenPlace(), adoptionWriteDto.getSpecialMark());

        // 나머지 Adoption 엔티티 설정
        Optional<Member> member = repository.findById(2L);
        Member member1 = member.get();
        adoption.setMember(member1);
        Adoption saved = adoptionRepository.save(adoption);

        for (MultipartFile file : files) {
            serverFileName = getString(file); // 미니오에 저장하는 영역
            AdoptionImage adoptionImage = new AdoptionImage(serverFileName, saved);

            adoptionImageRepository.save(adoptionImage);
        }


   }


    private String getString(MultipartFile file) {
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



    public boolean isBucketByUserId(String userId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, io.minio.errors.ServerException {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(userId).build());
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

    public List<Adoption> findAllWithImages() {
        return adoptionRepository.findAllWithImages();
    }

    public List<Adoption> findAllWithImagesAndMember() {
        return adoptionRepository.findAllWithImagesAndMember();
    }


}
