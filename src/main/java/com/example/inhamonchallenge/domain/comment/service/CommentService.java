package com.example.inhamonchallenge.domain.comment.service;

import com.example.inhamonchallenge.domain.comment.domain.Comment;
import com.example.inhamonchallenge.domain.comment.dto.*;
import com.example.inhamonchallenge.domain.comment.exception.NotFoundCommentException;
import com.example.inhamonchallenge.domain.comment.repository.CommentRepository;
import com.example.inhamonchallenge.domain.common.FeedType;
import com.example.inhamonchallenge.domain.common.dto.Result;
import com.example.inhamonchallenge.domain.common.exception.DeleteDeniedException;
import com.example.inhamonchallenge.domain.common.exception.UpdateDeniedException;
import com.example.inhamonchallenge.domain.user.domain.User;
import com.example.inhamonchallenge.domain.user.exception.NotFoundUserException;
import com.example.inhamonchallenge.domain.user.repository.UserRepository;
import com.example.inhamonchallenge.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public SaveCommentResponse addComment(SaveCommentRequest request) {
        User user = userRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(NotFoundUserException::new);
        if (request.getParentId() != null) {
            Comment parent = commentRepository.findById(request.getParentId()).orElseThrow(NotFoundCommentException::new);
            return SaveCommentResponse.from(commentRepository.save(SaveCommentRequest.toEntity(request, user, parent)));
        }
        return SaveCommentResponse.from(commentRepository.save(SaveCommentRequest.toEntity(request, user, null)));
    }

    public CommentResponse getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
        return CommentResponse.from(comment);
    }

    public Result<List<CommentWithReplyResponse>> getCommentList(FeedType feedType, Long feedId) {
        List<Comment> parentComments = commentRepository.findByFeedTypeAndFeedIdAndParentIsNullOrderByCreatedAt(feedType, feedId);
        List<CommentWithReplyResponse> responses = parentComments.stream()
                .map(parentComment -> {
                    List<CommentReplyResponse> childResponses = parentComment.getReplies().stream()
                            .sorted(Comparator.comparing(Comment::getCreatedAt))
                            .map(child -> CommentReplyResponse.from(child))
                            .collect(Collectors.toList());
                    return CommentWithReplyResponse.from(parentComment, childResponses);
                })
                .collect(Collectors.toList());

        return new Result<>(responses);
    }

    public SaveCommentResponse updateComment(Long commentId, UpdateCommentRequest request) {
        User user = userRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(NotFoundUserException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
        if (user.getId() != comment.getUser().getId()) {
            throw new UpdateDeniedException();
        }
        comment.update(request.getContent());
        return SaveCommentResponse.from(comment);
    }

    public void deleteComment(Long commentId) {
        User user = userRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(NotFoundUserException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
        if (user.getId() != comment.getUser().getId()) {
            throw new DeleteDeniedException();
        }
        commentRepository.delete(comment);
    }
}
