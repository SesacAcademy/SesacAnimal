package com.project.animal.missing.service.inf;

public interface MissingLikeCacheService {

  void updateLike(long postId, int status);

  int getCountByPostId(long postId);
}
