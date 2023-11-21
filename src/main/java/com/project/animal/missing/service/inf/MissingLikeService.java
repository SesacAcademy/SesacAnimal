package com.project.animal.missing.service.inf;

public interface MissingLikeService {
  int likePost(long memberId, long postId);

  int getStatusByPostIdAndMemberId(long postId, long memberId);

  int getLikeCount(long postId);
}
