package me.liiot.snsserver.mapper;

import me.liiot.snsserver.annotation.ClientDatabase;
import me.liiot.snsserver.model.post.Image;
import me.liiot.snsserver.model.post.ImageUploadInfo;
import me.liiot.snsserver.util.ClientDatabases;

import java.util.List;

public interface FileMapper {

    @ClientDatabase(value = ClientDatabases.MASTER)
    void insertImage(ImageUploadInfo imageUploadInfo);

    @ClientDatabase(value = ClientDatabases.MASTER)
    void insertImages(List<ImageUploadInfo> imageUploadInfos);

    @ClientDatabase(value = ClientDatabases.SLAVE)
    boolean isExistImages(int postId);

    @ClientDatabase(value = ClientDatabases.SLAVE)
    List<Image> getImages(int postId);

    @ClientDatabase(value = ClientDatabases.SLAVE)
    List<String> getImagePaths(int postId);

    @ClientDatabase(value = ClientDatabases.MASTER)
    void deleteImages(int postId);
}
