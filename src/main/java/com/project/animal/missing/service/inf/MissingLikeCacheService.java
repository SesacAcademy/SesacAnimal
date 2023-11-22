package com.project.animal.missing.service.inf;

import java.util.List;
import java.util.Optional;

public interface MissingLikeCacheService {
  void updateLike(long postId, int status);
  Optional<Integer> getCountByPostId(long postId);
  List<Integer> getCountsByPostIds(List<Long> postIds);
}
