package ru.project.services;

import org.springframework.data.domain.Page;
import ru.project.dto.PostDtoRequest;
import ru.project.dto.PostDtoResponse;
import ru.project.dto.PostDtoResponseShort;

import java.util.List;

public interface PostService {

    void savePost(final PostDtoRequest postDtoRequest);

    PostDtoResponse getPostById(final Long postId);

    List<PostDtoResponseShort> getPosts();

    List<PostDtoResponseShort> getPostsWithTags(final String tagText);

    Page<PostDtoResponseShort> getPostsPaginated(final Integer size,
                                                 final Integer page);

    void updatePost(final PostDtoRequest postDtoRequest,
                    final Long postId);

    void deletePost(final Long postId);

}
