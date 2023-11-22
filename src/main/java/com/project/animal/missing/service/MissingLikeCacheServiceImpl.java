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

  private final String cachePrefix = "missingLike";
  private final int ADD = 1;


  @Override
  public Optional<Integer> getCountByPostId(long postId) {
    String likeCountKey = cachePrefix + postId;
    Optional<String> maybeCurrentCount = redisServiceProvider.get(likeCountKey);

    Integer currentCount = maybeCurrentCount.isPresent()
            ? Integer.parseInt(maybeCurrentCount.get())
            : null;

    return Optional.ofNullable(currentCount);
  }

  @Override
  public void updateLike(long postId, int status) {
    String likeCountKey = cachePrefix + postId;

    if (status == ADD) {
      redisServiceProvider.increase(likeCountKey);
    } else {
      redisServiceProvider.decrease(likeCountKey);
    }
  }
}
