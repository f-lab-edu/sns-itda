package me.liiot.snsserver.mapper;

import me.liiot.snsserver.model.post.Image;
import me.liiot.snsserver.model.post.ImageUploadInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {

    void insertImage(ImageUploadInfo imageUploadInfo);

    void insertImages(List<ImageUploadInfo> imageUploadInfos);

    boolean isExistImages(int postId);

    List<Image> getImages(int postId);

    List<String> getImagePaths(int postId);

    void deleteImages(int postId);
}
