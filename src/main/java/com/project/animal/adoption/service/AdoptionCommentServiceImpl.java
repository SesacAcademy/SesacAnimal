package com.project.animal.adoption.service;

import com.project.animal.adoption.domain.Adoption;
import com.project.animal.adoption.domain.AdoptionComment;
import com.project.animal.adoption.dto.AdoptionCommentWriteDto;
import com.project.animal.adoption.repository.AdoptionCommentRepository;
import com.project.animal.adoption.repository.AdoptionRepository;
import com.project.animal.member.domain.Member;
import com.project.animal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdoptionCommentServiceImpl {

    private final AdoptionRepository adoptionRepository;
    private final AdoptionCommentRepository adoptionCommentRepository;
    private final MemberRepository repository;
    public void saveComment(AdoptionCommentWriteDto adoptionCommentDto, Long postId){

        AdoptionComment adoptionComment = new AdoptionComment(adoptionCommentDto);

        Optional<Member> member = repository.findById(2L);
        Member member1 = member.orElseThrow();
        adoptionComment.setMember(member1);
        Adoption adoption = adoptionRepository.findById(postId).orElseThrow();
        adoptionComment.setAdoption(adoption);

        adoptionCommentRepository.save(adoptionComment);

    }

    public Optional<AdoptionComment> findById(Long id){
        return adoptionCommentRepository.findById(id);
    }

    public List<AdoptionComment> findByAdoptionId (Long id) {
        return adoptionCommentRepository.findByAdoptionId(id);
    }

    public List<AdoptionComment> findTopLevelCommentsByAdoptionId(Long adoptionId) {
        List<AdoptionComment> topLevelComments = adoptionCommentRepository.findByAdoptionId(adoptionId);
        return filterOutReplies(topLevelComments);
    }

    private List<AdoptionComment> filterOutReplies(List<AdoptionComment> comments) {
        return comments.stream()
                .filter(comment -> comment.getParentId() == null)
                .collect(Collectors.toList());
    }


    public List<AdoptionComment> findAll(){
       return adoptionCommentRepository.findAll();
    }
}