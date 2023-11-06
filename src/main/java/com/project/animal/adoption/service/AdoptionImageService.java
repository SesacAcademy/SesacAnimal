package com.project.animal.adoption.service;

import com.project.animal.adoption.domain.Adoption;
import com.project.animal.adoption.domain.AdoptionImage;
import com.project.animal.adoption.repository.AdoptionImageRepository;
import com.project.animal.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdoptionImageService {

    private final AdoptionImageRepository adoptionImageRepository;

    public AdoptionImage save(AdoptionImage adoptionImage){

        return adoptionImage;
    }

    /*public Optional<AdoptionImage> findById(Adoption adoption){
        Optional<AdoptionImage> foundId = adoptionImageRepository.findById(adoption.getMember().getId());

        return foundId;
    }*/
//    public AdoptionImage findById(Long id){
//       return  adoptionImageRepository.findById(id).get();
//    }

//    public Optional<AdoptionImage> findByAdoptionId(Adoption adoption){
//
//        AdoptionImage byAdoptionId = adoptionImageRepository.findByAdoptionId(adoption.getId());
//        return byAdoptionId;
//    }


}
