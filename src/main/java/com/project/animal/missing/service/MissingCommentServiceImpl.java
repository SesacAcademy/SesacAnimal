package com.project.animal.missing.service;

import com.project.animal.member.domain.Member;
import com.project.animal.member.repository.MemberRepository;
import com.project.animal.missing.domain.MissingComment;
import com.project.animal.missing.domain.MissingPost;
import com.project.animal.missing.dto.comment.MissingCommentDeleteDto;
import com.project.animal.missing.dto.comment.MissingCommentEditDto;
import com.project.animal.missing.dto.comment.MissingCommentNewDto;
import com.project.animal.missing.exception.*;
import com.project.animal.missing.repository.MissingCommentRepository;
import com.project.animal.missing.repository.MissingPostRepository;
import com.project.animal.missing.service.inf.MissingCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MissingCommentServiceImpl implements MissingCommentService {

  private final MemberRepository memberRepository;
  private final MissingCommentRepository missingCommentRepository;
  private final MissingPostRepository missingPostRepository;

  public boolean createComment(long memberId, MissingCommentNewDto dto) {
    try {
      Optional<Member> maybeMember = memberRepository.findById(memberId);
      Member member = maybeMember.orElseThrow(() -> new RuntimeException("일치하는 회원이 존재하지 않습니다."));

      Optional<MissingPost> maybePost =  missingPostRepository.findById(dto.getMissingId());
      MissingPost post = maybePost.orElseThrow(() -> new DetailNotFoundException(dto.getMissingId()));

      MissingComment parentComment = null;
      if (dto.getParentId() != null) {
        Optional<MissingComment> maybeComment = missingCommentRepository.findById(dto.getParentId());
        parentComment = maybeComment.orElseThrow(() -> new RuntimeException("해당 부모 댓글이 없습니다."));
      }

      MissingComment comment = new MissingComment(member, post, dto.getComment(), parentComment);

      MissingComment result = missingCommentRepository.save(comment);
      if (result == null) throw new Exception("no save result");
      return true;
    } catch (Exception ex) {
      log.error("Error in createComment: >> " + dto.toString());
      throw new CommentSaveFailException(ex.getMessage(), ex.getCause(), dto);
    }
  }

  public boolean editComment(MissingCommentEditDto dto) {
    try {
      Optional<MissingComment> maybeComment = missingCommentRepository.findById(dto.getCommentId());
      MissingComment comment = maybeComment.orElseThrow(() -> new CommentNotFoundException(dto.getCommentId(), dto.getMissingId()));

      comment.changeComment(dto.getComment());
      MissingComment result = missingCommentRepository.save(comment);
      if (result == null) throw new Exception("no edit result");
      return true;
    } catch (Exception ex) {
      log.error("Error in editComment: >> " + dto.toString());
      throw new CommentEditFailException(ex.getMessage(), ex.getCause(), dto);
    }
  }

  @Transactional
  public boolean deleteComment(MissingCommentDeleteDto dto) {
    try {
      
      missingCommentRepository.deleteByParentCommentId(dto.getCommentId());
      missingCommentRepository.deleteById(dto.getCommentId());
      return true;
    } catch (Exception ex) {
      log.error("Error in deleteComment: >> " + dto.toString());
      throw new CommentDeleteFailException(ex.getMessage(), ex.getCause(), dto);
    }
  }
}
