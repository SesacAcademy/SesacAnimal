package com.project.animal.adoption.service;

import com.project.animal.adoption.domain.AdoptionComment;
import com.project.animal.adoption.dto.AdoptionCommentDto;
import com.project.animal.adoption.repository.AdoptionCommentRepository;
import com.project.animal.adoption.repository.AdoptionRepository;
import com.project.animal.member.domain.Member;
import com.project.animal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdoptionCommentServiceImpl {

    private final AdoptionRepository adoptionRepository;
    private final AdoptionCommentRepository adoptionCommentRepository;
    private final MemberRepository repository;
    public void saveComment(AdoptionCommentDto adoptionCommentDto){

        AdoptionComment adoptionComment = new AdoptionComment(adoptionCommentDto.getContent());

        Optional<Member> member = repository.findById(2L);
        Member member1 = member.get();
        adoptionComment.setMember(member1);

        adoptionCommentRepository.save(adoptionComment);

    }
}
