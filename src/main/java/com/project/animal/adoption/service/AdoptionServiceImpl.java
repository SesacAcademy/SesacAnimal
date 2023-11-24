package com.project.animal.adoption.service;

import com.project.animal.adoption.domain.Adoption;
import com.project.animal.adoption.domain.AdoptionImage;
import com.project.animal.adoption.domain.AdoptionPostLike;
import com.project.animal.adoption.dto.AdoptionEditDto;
import com.project.animal.adoption.dto.AdoptionWriteDto;
import com.project.animal.adoption.repository.AdoptionImageRepository;
import com.project.animal.adoption.repository.AdoptionPostLikeRepository;
import com.project.animal.adoption.repository.AdoptionRepository;
import com.project.animal.adoption.service.inf.AdoptionService;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.member.domain.Member;
import com.project.animal.member.exception.LoginException;
import com.project.animal.member.repository.MemberRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class AdoptionServiceImpl implements AdoptionService {


    private final AdoptionRepository adoptionRepository;
    private final AdoptionImageRepository adoptionImageRepository;
    private final AdoptionPostLikeRepository adoptionPostLikeRepository;
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



        for (MultipartFile file : files) {
            if(file.getSize()==0){
                System.out.println("입양 상세 페이지 수정 중 첨부된 file이 없습니다.");
                adoption.updateAdoption(adoptionEditDto);
            }else {
                if(adoption.getAdoptionImages().get(0).getPath().equals("empty")){
                    adoption.getAdoptionImages().get(0).changeIsActive(0);
                }
                adoption.updateAdoption(adoptionEditDto);
                Adoption saved = adoptionRepository.save(adoption);
                serverFileName = saveMinio(file); // 미니오에 저장하는 영역
                AdoptionImage adoptionImage = new AdoptionImage(serverFileName, saved);
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

//    public List<Adoption> getDogList() {
//        // Adoption 엔터티에서 breed 값이 "[개]"를 포함하는 데이터 검색
//        return adoptionRepository.findByBreedContaining("[개]");
//    }
//
//    public List<Adoption> getCatList() {
//        // Adoption 엔터티에서 breed 값이 "[고양이]"를 포함하는 데이터 검색
//        return adoptionRepository.findByBreedContaining("[고양이]");
//    }

    public Page<Adoption> findPatsByBreed(String breed, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber -1, pageSize);
        return adoptionRepository.findByBreedContaining(breed, pageable);
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




    @Transactional
    public int likeAdoption(Long postId, MemberDto memberDto) {
        Map<String,Object> responseBody = new HashMap<>();
        // 좋아요 로직 수행
        Adoption adoption = adoptionRepository.findById(postId).orElse(null);
        if (adoption != null) {
            Member member = repository.findById(memberDto.getId()).orElseThrow();
            AdoptionPostLike postLike = adoptionPostLikeRepository.findByAdoptionAndMember(adoption, member);

            if (postLike != null) {
                // 이미 좋아요를 눌렀다면 좋아요 취소
                adoptionPostLikeRepository.delete(postLike);
                adoptionPostLikeRepository.flush();

            } else {
                // 좋아요를 누르지 않았다면 좋아요 추가
                postLike = new AdoptionPostLike(adoption, member);

                adoptionPostLikeRepository.save(postLike);
            }
        }
        // 업데이트된 상태 반환
//        responseBody.put("status", getPostLikeByAdoptionId(postId).getStatus());

//        responseBody.put("likeCount", );
        // 업데이트된 좋아요 수 반환
        return adoption.getAdoptionPostLikes().size();
    }

    public boolean isAdoptionLikedByUser(Long postId, Long memberId) {
        Adoption adoption = adoptionRepository.findById(postId).orElse(null);

        if (adoption != null) {
            Member member = repository.findById(memberId).orElseThrow();
            AdoptionPostLike postLike = adoptionPostLikeRepository.findByAdoptionAndMember(adoption, member);
            return postLike != null;
        }

        return false;
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