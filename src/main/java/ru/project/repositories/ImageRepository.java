package ru.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.models.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
