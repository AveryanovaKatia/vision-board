package ru.project.dto;

import lombok.Data;

@Data
public class PostDtoResponseShort {

    private String postName;

    private String base64Image;

    private String postPreview;

    private Integer countLikes;

    private String tags;

    private Integer countComments;

}
