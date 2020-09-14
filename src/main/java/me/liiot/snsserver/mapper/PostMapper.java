package me.liiot.snsserver.mapper;

import me.liiot.snsserver.model.post.Post;
import me.liiot.snsserver.model.post.PostUploadInfo;

public interface PostMapper {

    void insertPost(PostUploadInfo postUploadInfo);

    int getPostId(String userId);

    Post getPost(int postId);
}
