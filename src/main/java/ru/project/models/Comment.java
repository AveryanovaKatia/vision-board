package ru.project.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Column(name = "comment_id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(name = "comment_text", nullable = false)
    private String commentText;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    public Comment(final String commentText,
                   final Long postId) {
        this.commentText = commentText;
        this.postId = postId;
    }
}