package me.liiot.snsserver.controller;

import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.annotation.CheckLogin;
import me.liiot.snsserver.annotation.CurrentUser;
import me.liiot.snsserver.exception.NotExistUserIdException;
import me.liiot.snsserver.model.post.Post;
import me.liiot.snsserver.model.user.User;
import me.liiot.snsserver.service.PostService;
import me.liiot.snsserver.util.HttpResponses;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static me.liiot.snsserver.util.HttpResponses.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    @CheckLogin
    public ResponseEntity<Void> uploadPost(@RequestParam String content,
                                           @RequestPart("image") List<MultipartFile> images,
                                           @CurrentUser User currentUser) {

        postService.uploadPost(currentUser, content, images);
        return RESPONSE_CREATED;
    }

    @GetMapping("/{postId}")
    @CheckLogin
    public ResponseEntity<Post> getPost(@PathVariable int postId) {

        Post post = postService.getPost(postId);

        if (post == null) {
            return RESPONSE_NOT_FOUND;
        }

        return ResponseEntity.ok(post);
    }

    @GetMapping
    @CheckLogin
    public ResponseEntity<List<Post>> getUserFeed(@RequestParam(value = "userId") String userId) {

        try {
            List<Post> posts = postService.getPostsByUser(userId);

            return ResponseEntity.ok(posts);
        } catch (NotExistUserIdException e) {
            return RESPONSE_NOT_FOUND;
        }
    }

    @PatchMapping("/{postId}")
    @CheckLogin
    public ResponseEntity<Void> updatePost(@PathVariable int postId,
                                           String content,
                                           @CurrentUser User currentUser) {

        try {
            postService.updatePost(currentUser, postId, content);

            return RESPONSE_OK;
        } catch (AccessException e) {
            return RESPONSE_UNAUTHORIZED;
        }
    }

    @DeleteMapping("/{postId}")
    @CheckLogin
    public ResponseEntity<Void> deletePost(@PathVariable int postId,
                                           @CurrentUser User currentUser) {

        try {
            postService.deletePost(currentUser, postId);

            return RESPONSE_OK;
        } catch (AccessException e) {
            return RESPONSE_UNAUTHORIZED;
        }
    }
}
