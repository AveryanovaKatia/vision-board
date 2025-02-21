package ru.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.models.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(final Long postId);

    Integer countByPostId(final Long postId);

}
