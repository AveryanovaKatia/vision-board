package ru.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostDtoRequest {

    @NotBlank(message = "Имя поста не может быть пустым.")
    @Size(min = 6, max = 254, message = "Имя поста должно быть не менее 6 и не более 254 символов.")
    private String postName;

    @NotNull(message = "Пост должен содержать картинку.")
    private MultipartFile multipartFile;

    @NotBlank(message = "Текст поста не может быть пустым.")
    @Size(min = 6, max = 8999, message = "Текст поста должен быть не менее 6 и не более 8999 символов.")
    private String postText;

    @NotBlank(message = "Должен быть добавлен хотя бы один тег")
    private String tagsText;
}
