package com.project.animal.missing.dto.comment;

import com.project.animal.missing.domain.MissingComment;
import lombok.Data;

import java.util.List;

@Data
public class MissingCommentListEntryDto {
  private long commentId;
  private long memberId;

  private String author;
  private long postId;
  private Long parentId;
  private String content;

  private List<MissingCommentListEntryDto> comments;

  private MissingCommentListEntryDto(long commentId, long memberId, String author, long postId, MissingComment parentComment, String content) {
    this.commentId = commentId;
    this.memberId = memberId;
    this.author = author;
    this.postId = postId;
    this.parentId = parentComment != null ? parentComment.getComment_id() : null;
    this.content = content;
  }

  public static MissingCommentListEntryDto fromMissingComment(long postId, MissingComment comment) {

    return new MissingCommentListEntryDto(
            comment.getComment_id(),
            comment.getMember().getId(),
            comment.getMember().getNickname(),
            postId,
            comment.getParentComment(),
            comment.getContent()
    );
  }

}
