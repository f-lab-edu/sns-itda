package me.liiot.snsserver.service;

import me.liiot.snsserver.model.post.Post;
import me.liiot.snsserver.model.user.User;
import org.springframework.expression.AccessException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    public void uploadPost(User user, String content, List<MultipartFile> images);

    public Post getPost(int postId);

    public List<Post> getPostsByUser(String userId);

    public void updatePost(User user, int postId, String content) throws AccessException;

    public void deletePost(User user, int postId) throws AccessException;
}
