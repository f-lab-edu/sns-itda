package me.liiot.snsserver.mapper;

import me.liiot.snsserver.model.post.PostImageUploadInfo;
import me.liiot.snsserver.model.post.PostUploadInfo;

import java.util.List;

public interface PostMapper {

    void insertPost(PostUploadInfo postUploadInfo);

    int getPostId(String userId);

    void insertImage(PostImageUploadInfo imageUploadInfo);

    void insertImages(List<PostImageUploadInfo> imageUploadInfos);
}
