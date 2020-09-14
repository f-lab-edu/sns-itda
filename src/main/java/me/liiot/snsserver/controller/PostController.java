package me.liiot.snsserver.controller;

import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.annotation.CheckLogin;
import me.liiot.snsserver.annotation.CurrentUser;
import me.liiot.snsserver.model.user.User;
import me.liiot.snsserver.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static me.liiot.snsserver.util.HttpResponses.RESPONSE_CREATED;

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
}
