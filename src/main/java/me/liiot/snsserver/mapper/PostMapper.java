package me.liiot.snsserver.mapper;

import me.liiot.snsserver.model.post.Post;
import me.liiot.snsserver.model.post.PostUploadInfo;

import java.util.List;

public interface PostMapper {

    void insertPost(PostUploadInfo postUploadInfo);

    int getLatestPostId(String userId);

    Post getPost(int postId);

    List<Post> getPostsByUserId(String userId);
}
