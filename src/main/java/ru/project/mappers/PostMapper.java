package ru.project.mappers;

import org.springframework.stereotype.Component;
import ru.project.dto.PostDtoRequest;
import ru.project.dto.PostDtoResponse;
import ru.project.dto.PostDtoResponseShort;
import ru.project.models.Image;
import ru.project.models.Post;

import java.util.Base64;
import java.util.List;

@Component
public class PostMapper {

    final static Integer PREVIEW_MAX_LENGTH = 5;

    public static Post toPost(final PostDtoRequest postDtoRequest,
                              final Long imageId) {

        final Post post = new Post();

        post.setPostName(postDtoRequest.getPostName());
        post.setPostText(postDtoRequest.getPostText());
        post.setImageId(imageId);

        return post;
    }

    public static PostDtoResponse toPostDtoResponse(final Post post,
                                                    final Image image,
                                                    final List<String> tags,
                                                    final Integer countLike,
                                                    final List<String> comments) {

        final PostDtoResponse postDtoResponse = new PostDtoResponse();

        final String imageBase64 = Base64.getEncoder().encodeToString(image.getImageData());

        postDtoResponse.setPostId(post.getPostId());
        postDtoResponse.setPostName(post.getPostName());
        postDtoResponse.setPostText(post.getPostText());
        postDtoResponse.setPostPreview(getPreview(post.getPostText()));
        postDtoResponse.setBase64Image(imageBase64);
        postDtoResponse.setTags(tags);
        postDtoResponse.setCountLikes(countLike);
        postDtoResponse.setComments(comments);

        return postDtoResponse;
    }

    public static PostDtoResponseShort toPostDtoResponseShort(final Post post,
                                                              final Image image,
                                                              final List<String> tags,
                                                              final Integer countLike,
                                                              final Integer countComment) {

        final PostDtoResponseShort postDtoResponseShort = new PostDtoResponseShort();

        final String imageBase64 = Base64.getEncoder().encodeToString(image.getImageData());

        postDtoResponseShort.setPostName(post.getPostName());
        postDtoResponseShort.setPostPreview(getPreview(post.getPostText()));
        postDtoResponseShort.setBase64Image(imageBase64);
        postDtoResponseShort.setCountLikes(countLike);
        postDtoResponseShort.setTags(tags.toString());
        postDtoResponseShort.setCountComments(countComment);

        return  postDtoResponseShort;
    }

    private static String getPreview(final String postText) {
        if (postText.isEmpty()) return "";
        int newlinePos = postText.indexOf("\n");
        int previewEnd = Math.min((newlinePos != -1 ? newlinePos + 1 : postText.length()), PREVIEW_MAX_LENGTH);
        return postText.substring(0, Math.min(previewEnd, postText.length()));
    }
}
