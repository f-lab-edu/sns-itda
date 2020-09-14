package me.liiot.snsserver.service;

import me.liiot.snsserver.mapper.FileMapper;
import me.liiot.snsserver.mapper.PostMapper;
import me.liiot.snsserver.model.FileInfo;
import me.liiot.snsserver.model.post.*;
import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.model.post.PostUploadInfo;
import me.liiot.snsserver.model.user.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;

    private final FileService fileService;

    @Override
    public void uploadPost(User user, String content, List<MultipartFile> images) {
        PostUploadInfo postUploadInfo = new PostUploadInfo(user.getUserId(), content);

        postMapper.insertPost(postUploadInfo);
        int postId = postMapper.getLatestPostId(user.getUserId());

        if (!images.isEmpty()) {
            if (images.size() == 1) {
                FileInfo fileInfo = fileService.uploadFile(images.get(0), user.getUserId());
                fileService.uploadImage(postId, fileInfo);
            } else {
                List<FileInfo> fileInfos = fileService.uploadFiles(images, user.getUserId());
                fileService.uploadImages(postId, fileInfos);
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
