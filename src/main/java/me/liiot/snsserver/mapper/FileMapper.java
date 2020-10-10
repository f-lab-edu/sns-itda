package me.liiot.snsserver.mapper;

import me.liiot.snsserver.model.post.Image;
import me.liiot.snsserver.model.post.ImageUploadInfo;

import java.util.List;

public interface FileMapper {

    void insertImage(ImageUploadInfo imageUploadInfo);

    void insertImages(List<ImageUploadInfo> imageUploadInfos);

    boolean isExistImages(int postId);

    List<Image> getImages(int postId);

    List<String> getImagePaths(int postId);

    void deleteImages(int postId);
}
