package me.liiot.snsserver.mapper;

import me.liiot.snsserver.model.post.ImageUploadInfo;

import java.util.List;

public interface FileMapper {

    void insertImage(ImageUploadInfo imageUploadInfo);

    void insertImages(List<ImageUploadInfo> imageUploadInfos);
}
