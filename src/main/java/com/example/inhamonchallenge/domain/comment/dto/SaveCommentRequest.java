package com.example.inhamonchallenge.domain.comment.dto;

import com.example.inhamonchallenge.domain.comment.domain.Comment;
import com.example.inhamonchallenge.domain.common.FeedType;
import com.example.inhamonchallenge.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SaveCommentRequest {

    private String content;

    private FeedType feedType;

    private Long feedId;

    private Long parentId;

    public static Comment toEntity(SaveCommentRequest request, User user, Comment comment) {
        return Comment.builder()
                .user(user)
                .comment(comment)
                .feedId(request.getFeedId())
                .content(request.getContent())
                .feedType(request.getFeedType())
                .reportCnt(0)
                .build();
    }
}
