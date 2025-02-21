package ru.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.models.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Integer countByPostId(final Long postId);

}
