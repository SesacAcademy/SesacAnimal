package com.project.animal.missing.service;

import com.project.animal.global.common.provider.RedisServiceProvider;
import com.project.animal.missing.repository.MissingLikeRepository;
import com.project.animal.missing.service.inf.MissingLikeCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MissingLikeCacheServiceImpl implements MissingLikeCacheService {
  private final RedisServiceProvider redisServiceProvider;

  private final MissingLikeRepository missingLikeRepository;

  private final String cachePrefix = "missingLike";
  private final int ADD = 1;

  @Override
  public void update(long postId, int status) {
    String likeCountKey = cachePrefix + postId;
    Optional<String> maybeCurrentCount = redisServiceProvider.get(likeCountKey);

    int currentCount = maybeCurrentCount.isPresent()
            ? Integer.parseInt(maybeCurrentCount.get())
            : missingLikeRepository.likedCountByPostId(postId);

    int nextCount = status == ADD
            ? addCount(currentCount)
            : subCount(currentCount);

    redisServiceProvider.save(likeCountKey, nextCount);
  }


  private int addCount(int currentCount) {
    return currentCount + 1;
  }

  private int subCount(int currentCount) {
    return currentCount > 0 ? currentCount - 1 : 0;
  }

}
