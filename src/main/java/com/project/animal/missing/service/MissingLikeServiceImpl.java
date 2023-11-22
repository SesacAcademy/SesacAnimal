package com.project.animal.missing.service;

import com.project.animal.member.domain.Member;
import com.project.animal.member.exception.NotFoundException;
import com.project.animal.member.repository.MemberRepository;
import com.project.animal.missing.domain.MissingLike;
import com.project.animal.missing.domain.MissingPost;
import com.project.animal.missing.exception.DetailNotFoundException;
import com.project.animal.missing.repository.MissingLikeRepository;
import com.project.animal.missing.repository.MissingPostRepository;
import com.project.animal.missing.service.inf.MissingLikeCacheService;
import com.project.animal.missing.service.inf.MissingLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MissingLikeServiceImpl implements MissingLikeService {

  private final MissingLikeRepository missingLikeRepository;
  private final MemberRepository memberRepository;
  private final MissingPostRepository missingPostRepository;
  private final MissingLikeCacheService missingLikeCacheService;


  @Override
  public int getLikeCount(long postId) {
    Optional<Integer> maybeCurrentCount = missingLikeCacheService.getCountByPostId(postId);
    Integer currentCount = maybeCurrentCount.orElse(missingLikeRepository.likedCountByPostId(postId));

    return currentCount;
  }

  @Override
  public int getStatusByPostIdAndMemberId(long postId, long memberId) {
    final int NotLiked = 0;
    Optional<MissingLike> maybeLike = missingLikeRepository.findLikeByPostIdAndMemberId(postId, memberId);
    MissingLike like = maybeLike.orElse(null);

    return like != null ? like.getStatus() : NotLiked;
  }

  @Transactional
  @Override
  public int likePost(long memberId, long postId) {
    Optional<MissingLike> maybeLike = missingLikeRepository.findLikeByPostIdAndMemberId(postId, memberId);
    MissingLike like = maybeLike.isEmpty()
            ? createMissingLike(memberId, postId)
            : updateMissingLike(maybeLike.get());

    missingLikeRepository.save(like);
    missingLikeCacheService.updateLike(postId, like.getStatus());

    return like.getStatus();
  }

  private MissingLike createMissingLike(long memberId, long postId) {
    Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException());

    MissingPost post = missingPostRepository.findById(postId)
            .orElseThrow(() -> new DetailNotFoundException(postId));

    return  new MissingLike(post, member, 1);
  }

  private MissingLike updateMissingLike(MissingLike like) {
    like.toggleStatus();
    return like;
  }

}
