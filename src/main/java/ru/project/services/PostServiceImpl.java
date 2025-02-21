package ru.project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.project.dto.PostDtoRequest;
import ru.project.dto.PostDtoResponse;
import ru.project.dto.PostDtoResponseShort;
import ru.project.exceptions.ImageProcessingException;
import ru.project.exceptions.NotFoundException;
import ru.project.mappers.PostMapper;
import ru.project.models.Image;
import ru.project.models.Post;
import ru.project.models.Tag;
import ru.project.repositories.ImageRepository;
import ru.project.repositories.PostRepository;
import ru.project.repositories.TagRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final ImageRepository imageRepository;

    private final TagRepository tagRepository;

    private final FeedbackService feedbackService;

    @Autowired
    public PostServiceImpl(final PostRepository postRepository,
                           final ImageRepository imageRepository,
                           final TagRepository tagRepository,
                           final FeedbackService feedbackService) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.tagRepository = tagRepository;
        this.feedbackService = feedbackService;

    }

    @Override
    public void savePost(final PostDtoRequest postDtoRequest) {

        log.info("Запрос на добавление нового поста");

        final Image image = saveImage(postDtoRequest.getMultipartFile());
        final Post post = PostMapper.toPost(postDtoRequest, image.getImageId());
        final Post savedPost = postRepository.save(post);
        final Long savedPostId = savedPost.getPostId();

        saveTags(postDtoRequest.getTagsText(), savedPost.getPostId());

        log.info("Пост с id: {} загружен.", savedPostId);
    }

    @Override
    public PostDtoResponse getPostById(final Long postId) {

        log.info("Запрос на получение поста с id: {}.", postId);

        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Пост с id: " + postId + " не найден."));
        final Image image = getImage(post.getImageId());
        final Integer countLike = feedbackService.getCountLikesOfPost(post.getPostId());
        final List<String> comments = feedbackService.findAllCommentsByPostId(postId);
        final List<String> tags = findAllTagsByPostId(post.getPostId());
        final PostDtoResponse postDtoResponse =
                PostMapper.toPostDtoResponse(post, image, tags, countLike, comments);

        log.info("Пост с id: {} найден.", postDtoResponse.getPostId());
        return postDtoResponse;
    }

    @Override
    public List<PostDtoResponseShort> getPosts() {

        log.info("Запрос на получение ленты постов");

        return postRepository.findAll().stream()
                .map((post -> {
                    final Image image = getImage(post.getImageId());
                    final Integer countLike = feedbackService.getCountLikesOfPost(post.getPostId());
                    final Integer countComment = feedbackService.getCountCommentsOfPost(post.getPostId());
                    final List<String> tags = findAllTagsByPostId(post.getPostId());
                    final PostDtoResponseShort postDtoResponseShort =
                            PostMapper.toPostDtoResponseShort(post, image, tags, countLike, countComment);
                    log.info("Ленты постов успешно получена");
                    return postDtoResponseShort;
                }))
                .toList();
    }

    @Override
    public List<PostDtoResponseShort> getPostsWithTags(final String tagText) {

        log.info("Запрос на получение постов по тегу");

        final List<Long> postIds = findAllPostIdByTagText(tagText);
        return postRepository.findByPostIdIn(postIds).stream()
                .map((post -> {
                    final Image image = getImage(post.getImageId());
                    final Integer countLike = feedbackService.getCountLikesOfPost(post.getPostId());
                    final Integer countComment = feedbackService.getCountCommentsOfPost(post.getPostId());
                    final List<String> tags = findAllTagsByPostId(post.getPostId());
                    final PostDtoResponseShort postDtoResponseShort =
                            PostMapper.toPostDtoResponseShort(post, image, tags, countLike, countComment);
                    return postDtoResponseShort;
                }))
                .toList();
    }

    @Override
    public Page<PostDtoResponseShort> getPostsPaginated(final Integer size,
                                                        final Integer page) {

        log.info("Запрос на получение постов по {} на странице ", size);

        final Pageable pageable =
                PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        final Page<Post> postPage = postRepository.findAll(pageable);
        return postPage.map(post -> {
            final Image image = getImage(post.getImageId());
            final Integer countLike = feedbackService.getCountLikesOfPost(post.getPostId());
            final Integer countComment = feedbackService.getCountCommentsOfPost(post.getPostId());
            final  List<String> tags = findAllTagsByPostId(post.getPostId());
            final PostDtoResponseShort postDtoResponseShort =
                    PostMapper.toPostDtoResponseShort(post, image, tags, countLike, countComment);

            log.info("Посты получены");
            return postDtoResponseShort;
        });
    }

    @Override
    public void updatePost(final PostDtoRequest postDtoRequest,
                           final Long postId) {

        log.info("Запрос на обновление поста с id: {}.", postId);

        postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Пост с id: " + postId + " не найден."));
        final Image image = saveImage(postDtoRequest.getMultipartFile());
        final Post post = PostMapper.toPost(postDtoRequest, image.getImageId());
        final Post updatePost = postRepository.save(post);
        final Long updatePostId = updatePost.getPostId();
        saveTags(postDtoRequest.getTagsText(), updatePostId);

        log.info("Пост с id: {} обновлён.", updatePostId);
    }

    @Override
    public void deletePost(final Long postId) {

        log.info("Запрос на удаление поста с id: {}.", postId);

        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Пост с id: " + postId + " не найден."));
        postRepository.delete(post);

        log.info("Пост с id: {} удалён.", postId);
    }



    private Image saveImage(final MultipartFile file) {

        log.info("Запрос на загрузку изображения");

        final Image image;

        try {
            image = imageRepository.save(new Image(file.getBytes(), file.getOriginalFilename()));
        } catch (IOException exception) {
            log.error("Ошибка при обработке изображения: {}", exception.getMessage());
            throw new ImageProcessingException("Не удалось обработать изображение.");
        }

        log.info("Изображение успешно сохранено с ID: {}", image.getImageId());
        return image;
    }

    private Image getImage(final Long imageId) {

        log.info("Запрос на получение изображения с id: {}", imageId);

        final Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Изображение не найдено."));

        log.info("Изображение с id {} успешно найдено. Имя файла: {}", imageId, image.getImageName());
        return image;
    }

    private void saveTags(final String tagsText, final Long postId) {

        log.info("Запрос на загрузку тегов для поста с id: {}.", postId);

        final Set<String> tagsSet= Arrays.stream(tagsText.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
        final List<Tag> tagsList = tagsSet.stream()
                .map(tagText -> {
                    final Tag tag = new Tag();
                    tag.setTagText(tagText);
                    tag.setPostId(postId);
                    return tag;
                })
                .toList();
        final List<Tag> savedTags = tagRepository.saveAll(tagsList);
        savedTags.forEach(tag ->
                log.info("Тег c id: {} загружен для поста {}", tag.getTagId(), postId)
        );

        log.info("Теги загружены.");
    }

    private List<String> findAllTagsByPostId(final Long postId) {

        log.info("Запрос на получение всех тегов для поста с id: {}.", postId);

         final List<Tag> tags = tagRepository.findByPostId(postId);

         return tags.stream().map(Tag::getTagText).toList();
    }

    private List<Long> findAllPostIdByTagText(final String tagText) {

        log.info("Запрос на получение все постов для тега с id: {}.", tagText);

        return tagRepository.findByTagText(tagText).stream()
                .map(Tag::getPostId)
                .toList();
    }
}
