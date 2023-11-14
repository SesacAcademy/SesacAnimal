package com.project.animal.adoption.service;

import com.project.animal.adoption.domain.Adoption;
import com.project.animal.adoption.domain.AdoptionImage;
import com.project.animal.adoption.dto.AdoptionEditDto;
import com.project.animal.adoption.dto.AdoptionWriteDto;
import com.project.animal.adoption.repository.AdoptionImageRepository;
import com.project.animal.adoption.repository.AdoptionRepository;
import com.project.animal.adoption.service.inf.AdoptionService;
import com.project.animal.sample.openApi.dto.OpenApiDto;
import com.project.animal.member.domain.Member;
import com.project.animal.member.repository.MemberRepository;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    public void save(AdoptionWriteDto adoptionWriteDto, List<MultipartFile> files) {

        String serverFileName;
        Adoption adoption = new Adoption(adoptionWriteDto.getTitle(), adoptionWriteDto.getBreed(), adoptionWriteDto.getGender(), adoptionWriteDto.getAge(), adoptionWriteDto.getCenter(), adoptionWriteDto.getNeutered(), adoptionWriteDto.getContent(), adoptionWriteDto.getColor(), adoptionWriteDto.getHappenPlace(), adoptionWriteDto.getSpecialMark());

        // 나머지 Adoption 엔티티 설정 (멤버 강제주입 추후 변경 필요)
        Optional<Member> member = repository.findById(2L);
        Member member1 = member.orElseThrow();
        adoption.setMember(member1);
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
    public void update (AdoptionEditDto adoptionEditDto, List<MultipartFile> files, Long id) {
        String serverFileName;
        Adoption adoption = adoptionRepository.findByIdWithImageAndMember(id);

        adoption.updateAdoption(adoptionEditDto);

        // 나머지 Adoption 엔티티 설정 (멤버 강제주입 추후 변경 필요)
        Optional<Member> member = repository.findById(2L);
        Member member1 = member.get();
        adoption.setMember(member1);

        Adoption saved = adoptionRepository.save(adoption);

        for (MultipartFile file : files) {
            serverFileName = saveMinio(file); // 미니오에 저장하는 영역
            AdoptionImage adoptionImage = new AdoptionImage(serverFileName, saved);

            adoptionImageRepository.save(adoptionImage);
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

    public Adoption findByIdWidhImageAndMember(Long id){
        return adoptionRepository.findByIdWithImageAndMember(id);
    }

    public List<Adoption> findAllWithImages() {
        return adoptionRepository.findAllWithImages();
    }

    public List<Adoption> findAllWithImagesAndMember() {
        return adoptionRepository.findAllWithImagesAndMember();

    }

//    public  List<Adoption> test() {
       // return adoptionRepository.findAllWithImagesAndMember();
      //  List<Adoption>  list =   adoptionRepository.findAll();
      //  return list;

//        List<Adoption>  list =  adoptionRepository.findAllWithImagesAndMember();
//        for(Adoption a: list){
//           // adoptionImageRepository.findBy a.getId()
//            a.setAdoptionImages(adoptionImageRepository. test( a.getId() ) );
//        }
//
//       return adoptionRepository.test2();


//    }


}
