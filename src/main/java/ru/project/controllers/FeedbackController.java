package ru.project.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.project.services.FeedbackService;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/feed")
public class FeedbackController {

    FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }


    @PostMapping("/post/{id}/addLike")
    @ResponseStatus(HttpStatus.CREATED)
    public String saveLike(@PathVariable(name = "id") final Long postId) {
        feedbackService.saveLike(postId);
        return "redirect:/feed/post/" + postId;
    }

    @PostMapping("/post/{id}/saveComment")
    @ResponseStatus(HttpStatus.CREATED)
    public String saveComment(@PathVariable(name = "id") final Long postId,
                              @RequestParam(name = "text") final String commentText) {
        feedbackService.saveComment(commentText, postId);
        return "redirect:/feed/post/" + postId;
    }

    @PostMapping(value = "/post/comment")
    public String updateComment(@RequestParam(name = "id") final Long commentId,
                                @RequestParam(name = "text") final String commentText,
                                @RequestParam(name = "postId") final Long postId) {
        feedbackService.updateComment(commentId, commentText);
        return "redirect:/feed/post/" + postId;
    }

    @PostMapping(value = "/post/{id}/deleteComment/{commentId}", params = "_method=delete")
    public String deleteComment(@PathVariable(name = "commentId") final Long commentId,
                                @PathVariable(name = "id") final Long postId) {
        feedbackService.deleteComment(commentId);
        return "redirect:/feed/post/" + postId;
    }

}
