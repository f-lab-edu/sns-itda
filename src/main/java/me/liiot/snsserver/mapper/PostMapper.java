package me.liiot.snsserver.mapper;

import me.liiot.snsserver.model.post.Post;
import me.liiot.snsserver.model.post.PostUploadInfo;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostMapper {

    void insertPost(PostUploadInfo postUploadInfo);

    int getLatestPostId(String userId);

    Post getPost(int postId);

    List<Post> getPostsByUserId(String userId);

    boolean isAuthorizedOnPost(@Param("userId") String userId, @Param("postId") int postId);

    void updatePost(@Param("postId") int postId, @Param("content") String content);

    void deletePost(int postId);
}
