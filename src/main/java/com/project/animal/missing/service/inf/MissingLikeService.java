package com.project.animal.missing.service.inf;

import java.util.List;

public interface MissingLikeService {
  int likePost(long memberId, long postId);
  int getStatusByPostIdAndMemberId(long postId, long memberId);
  int getLikeCount(long postId);

  List<Integer> getLikeCountMultiple(List<Long> postIds);
}
