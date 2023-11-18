package com.project.animal.review.service.mapper;

import com.project.animal.member.domain.Member;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.domain.ReviewPostLike;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@NoArgsConstructor
@Component
public class ReviewPostLikeMapper {
    public ReviewPostLike createEntity(Member member, ReviewPost reviewPost) {
        return new ReviewPostLike(member, reviewPost);
    }
}
