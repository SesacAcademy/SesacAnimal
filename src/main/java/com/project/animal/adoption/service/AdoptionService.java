package com.project.animal.adoption.service;

import com.project.animal.adoption.domain.Adoption;
import com.project.animal.adoption.domain.AdoptionImage;
import com.project.animal.adoption.dto.AdoptionWriteDto;
import com.project.animal.adoption.repository.AdoptionImageRepository;
import com.project.animal.adoption.repository.AdoptionRepository;
import com.project.animal.member.domain.Member;
import com.project.animal.member.repository.MemberRepository;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
public class AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final AdoptionImageRepository adoptionImageRepository;
    private final MinioClient minioClient;
    private final MemberRepository repository;



   public Adoption save(AdoptionWriteDto adoptionWriteDto, MultipartFile file) {


       Adoption save = null;
       try {

           // 4. 파일 업로드 (바이너리)
            String serverFileName;
           if(file.isEmpty()){
               serverFileName = "empty";
           }else{
               serverFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
               InputStream is = file.getInputStream();

               minioClient.putObject(
                       PutObjectArgs.builder()
                               .bucket("adoption")
                               .object(serverFileName)
                               .stream(is, is.available(), 0)
                               .build()
               );
               is.close();
           }

           Adoption adoption = new Adoption(adoptionWriteDto.getTitle(),adoptionWriteDto.getBreed(),adoptionWriteDto.getGender(),adoptionWriteDto.getAge(),adoptionWriteDto.getCenter(),adoptionWriteDto.getNeutered(),adoptionWriteDto.getContent());

           AdoptionImage adoptionImage = new AdoptionImage(serverFileName, adoption);

           Optional<Member> member = repository.findById(1L);
           Member member1 = member.get();
           adoption.setMember(member1);

           Adoption saved = adoptionRepository.save(adoption);


           adoptionImage.setAdoption(saved);
           adoptionImageSave(adoptionImage);


       } catch (Exception e) {
           new RuntimeException(e);
       } finally {

       }


       return save;

   }

   public void adoptionImageSave(AdoptionImage adoptionImage) {
       adoptionImageRepository.save(adoptionImage);

   }

    public Optional<Adoption> findById(Long id) {

        Optional<Adoption> foundAdoptionId = adoptionRepository.findById(id);
        if (foundAdoptionId.isPresent()) {
            Adoption adoption = foundAdoptionId.get();
//            int hit = adoption.getHit();
//            hit++;
//            adoption.setHit(hit);
//            adoptionRepository.plusView(adoption);
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
        adoptionRepository.plusView(foundAdoptionId.get());

    }
}
