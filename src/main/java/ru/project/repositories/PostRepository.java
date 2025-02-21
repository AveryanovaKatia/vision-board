package ru.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.models.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByPostIdIn(final List<Long> postIds);

}
