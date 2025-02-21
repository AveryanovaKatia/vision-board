package ru.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.models.Tag;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByPostId(final Long postId);

    List<Tag> findByTagText(final String tagText);

}
