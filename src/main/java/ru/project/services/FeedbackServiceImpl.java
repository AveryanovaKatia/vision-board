package ru.project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.exceptions.NotFoundException;
import ru.project.models.Comment;
import ru.project.models.Like;
import ru.project.repositories.CommentRepository;
import ru.project.repositories.LikeRepository;
import ru.project.repositories.PostRepository;

import java.util.List;

@Slf4j
@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    PostRepository postRepository;

    LikeRepository likeRepository;

    CommentRepository commentRepository;

    @Autowired
    public FeedbackServiceImpl(final PostRepository postRepository,
                               final CommentRepository commentRepository,
                               final LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
    }

    @Override
    public void saveLike(final Long postId) {

        log.info("Запрос на добавление лайка к посту с id: {}.", postId);

        postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Пост с id: " + postId + " не найден."));
        final Like like = likeRepository.save(new Like(postId));

        log.info("Лайк c id {} добавлен к посту.", like.getLikeId());
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getCountLikesOfPost(final Long postId) {

        log.info("Запрос на получение количества лайков к посту с id: {}.", postId);

        postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Пост с id: " + postId + " не найден."));
        final Integer countLike =  likeRepository.countByPostId(postId);

        log.info("У поста с id: {} отмечено {} лайков", postId, countLike);
        return countLike;
    }

    @Override
    public void saveComment(final String commentText, final Long postId) {

        log.info("Запрос на сохранение комментария для поста с id: {}.", postId);

        postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Пост с id: " + postId + " не найден."));
        final Comment comment = commentRepository.save(new Comment(commentText, postId));

        log.info("Добавлен комментарий с id: {}.", comment.getCommentId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findAllCommentsByPostId(final Long postId) {

        log.info("Запрос на получение всех комментариев для поста с id: {}.", postId);

        final List<Comment> comments = commentRepository.findByPostId(postId);

        log.info("комментарии получены");

        return comments.stream()
                .map(Comment::getCommentText)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getCountCommentsOfPost(final Long postId) {

        log.info("Запрос на получение количества комментариев к посту с id: {}.", postId);
        final Integer countComment =  commentRepository.countByPostId(postId);

        log.info("У поста с id: {} оставлено {} комментариев", postId, countComment);
        return countComment;
    }

    @Override
    public void updateComment(final Long commentId, final String commentText) {

        log.info("Запрос на обновление комментария с id: {}", commentId);

        final Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id: {} не найден" + commentId));
        comment.setCommentText(commentText);
        commentRepository.save(comment);

        log.info("Комментарий с id: {} успешно обновлен", commentId);
    }

    @Override
    public void deleteComment(final Long commentId) {

        log.info("Запрос на удаление комментария с id: {}", commentId);

        commentRepository.deleteById(commentId);

        log.info("Комментарий с id: {} успешно удален", commentId);
    }
}
