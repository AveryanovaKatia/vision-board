package ru.project.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.project.dto.PostDtoRequest;
import ru.project.dto.PostDtoResponse;
import ru.project.dto.PostDtoResponseShort;
import ru.project.services.PostService;

import java.util.List;

@Controller
public class PostController {

    PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String savePost(@Valid @ModelAttribute final PostDtoRequest postDtoRequest) {
        postService.savePost(postDtoRequest);
        return "redirect:/feed";
    }

    @GetMapping("/post/{id}")
    public String getPostById(@PathVariable(name = "id") final Long postId, final Model model) {
        final PostDtoResponse postDtoResponse = postService.getPostById(postId);
        model.addAttribute("postDto", postDtoResponse);
        return "post";
    }

    @GetMapping
    public String getPosts(final Model model) {
        List<PostDtoResponseShort> feed = postService.getPosts();
        model.addAttribute("feed", feed);
        return "feed";
    }

    @GetMapping("/tags/")
    public String getPostsWithTags(@RequestParam(name = "tagText") final String tagText,
                                   final Model model) {
        model.addAttribute("feed", postService.getPostsWithTags(tagText));
        return "feed";
    }

    @GetMapping("/feed")
    public String getPostsPaginated(@RequestParam(defaultValue = "10") @Min(10) final Integer size,
                                    @RequestParam(defaultValue = "1") @Min(1) final Integer page,
                                    final Model model) {
        final Page<PostDtoResponseShort> postPage = postService.getPostsPaginated(size, page);
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("pageInfo", postPage);
        return "feed";
    }

    @PostMapping("/post/{id}/update")
    public String updatePost(@ModelAttribute final PostDtoRequest postDtoRequest,
                             @PathVariable(name = "id") final Long postId) {
        postService.updatePost(postDtoRequest, postId);
        return "redirect:/feed/post/" + postId;
    }

    @PostMapping(value = "/post/{id}", params = "_method=delete")
    public String deletePost(@PathVariable(name = "id") final Long postId) {
        postService.deletePost(postId);
        return "redirect:/feed";
    }
}
