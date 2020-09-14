package me.liiot.snsserver.mapper;

import me.liiot.snsserver.model.post.PostUploadInfo;

public interface PostMapper {

    void insertPost(PostUploadInfo postUploadInfo);

    int getLatestPostId(String userId);
}
