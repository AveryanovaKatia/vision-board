package ru.project.services;

import java.util.List;

public interface FeedbackService {

    void saveLike(final Long postId);

    Integer getCountLikesOfPost(final Long postId);

    void saveComment(final String commentText,
                     final Long postId);

    List<String> findAllCommentsByPostId(final Long postId);

    Integer getCountCommentsOfPost(final Long postId);

    void updateComment(final Long commentId,
                       final String commentText);

    void deleteComment(final Long commentId);

   }
