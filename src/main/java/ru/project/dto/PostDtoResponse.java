package ru.project.dto;

import lombok.Data;
import java.util.List;

@Data
public class PostDtoResponse {

    private Long postId;

    private String postName;

    private String postText;

    private String base64Image;

    private String postPreview;

    private Integer countLikes;

    private List<String> tags;

    private List<String> comments;

}
