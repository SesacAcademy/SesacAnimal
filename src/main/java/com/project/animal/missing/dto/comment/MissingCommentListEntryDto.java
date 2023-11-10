package com.project.animal.missing.dto.comment;

import com.project.animal.missing.domain.MissingComment;
import lombok.Data;

import java.util.List;

@Data
public class MissingCommentListEntryDto {
  private long commentId;
  private long memberId;
  private long postId;
  private Long parentId;
  private String content;

  private List<MissingCommentListEntryDto> comments;

  private MissingCommentListEntryDto(long commentId, long memberId, long postId, Long parentId, String content) {
    this.commentId = commentId;
    this.memberId = memberId;
    this.postId = postId;
    this.parentId = parentId;
    this.content = content;
  }

  public static MissingCommentListEntryDto fromMissingComment(long postId, MissingComment comment) {

    return new MissingCommentListEntryDto(
            comment.getComment_id(),
            comment.getMember_id(), postId,
            comment.getParentId(),
            comment.getContent()
    );
  }

}
