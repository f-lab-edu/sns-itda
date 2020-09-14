package me.liiot.snsserver.service;

import me.liiot.snsserver.mapper.FileMapper;
import me.liiot.snsserver.mapper.PostMapper;
import me.liiot.snsserver.model.FileInfo;
import me.liiot.snsserver.model.post.*;
import me.liiot.snsserver.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private PostMapper postMapper;

    private FileMapper fileMapper;

    private FileService fileService;

    @Autowired
    public PostServiceImpl(PostMapper postMapper, FileMapper fileMapper, FileService fileService) {
        this.postMapper = postMapper;
        this.fileMapper = fileMapper;
        this.fileService = fileService;
    }

    @Override
    public void uploadPost(User user, String content, List<MultipartFile> images) {
        PostUploadInfo postUploadInfo = new PostUploadInfo(user.getUserId(), content);

        postMapper.insertPost(postUploadInfo);
        int postId = postMapper.getPostId(user.getUserId());

        if (!images.isEmpty()) {
            if (images.size() == 1) {
                FileInfo fileInfo = fileService.uploadFile(images.get(0), user.getUserId());
                ImageUploadInfo imageUploadInfo =
                        new ImageUploadInfo(
                                postId,
                                fileInfo.getFileName(),
                                fileInfo.getFilePath(), 1);

                fileMapper.insertImage(imageUploadInfo);
            } else {
                List<FileInfo> fileInfos = fileService.uploadFiles(images, user.getUserId());

                List<ImageUploadInfo> imageUploadInfos = new ArrayList<>();
                int fileInfosLen = fileInfos.size();
                for (int i=0; i<fileInfosLen; i++) {
                    ImageUploadInfo imageUploadInfo =
                            new ImageUploadInfo(
                                    postId,
                                    fileInfos.get(i).getFileName(),
                                    fileInfos.get(i).getFilePath(),
                                    i+1);
                    imageUploadInfos.add(imageUploadInfo);
                }

                fileMapper.insertImages(imageUploadInfos);
            }
        }
    }

    @Override
    public Post getPost(int postId) {

        boolean isExistImages = fileService.isExistImages(postId);
        if (isExistImages) {
            List<Image> images = fileService.getImages(postId);
            Post postWithNoImages = postMapper.getPost(postId);

            Post post = Post.builder()
                    .id(postWithNoImages.getId())
                    .userId(postWithNoImages.getUserId())
                    .content(postWithNoImages.getContent())
                    .createTime(postWithNoImages.getCreateTime())
                    .images(images)
                    .build();

            return post;
        } else {
            Post post = postMapper.getPost(postId);

            return post;
        }
    }
}
