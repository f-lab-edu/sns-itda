package me.liiot.snsserver.service;

import me.liiot.snsserver.mapper.PostMapper;
import me.liiot.snsserver.model.FileInfo;
import me.liiot.snsserver.model.post.PostImageUploadInfo;
import me.liiot.snsserver.model.post.PostUploadInfo;
import me.liiot.snsserver.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private PostMapper postMapper;

    private FileService fileService;

    @Autowired
    public PostServiceImpl(PostMapper postMapper, FileService fileService) {
        this.postMapper = postMapper;
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
                PostImageUploadInfo imageUploadInfo =
                        new PostImageUploadInfo(
                                postId,
                                fileInfo.getFileName(),
                                fileInfo.getFilePath(), 1);

                postMapper.insertImage(imageUploadInfo);
            } else {
                List<FileInfo> fileInfos = fileService.uploadFiles(images, user.getUserId());

                List<PostImageUploadInfo> imageUploadInfos = new ArrayList<>();
                int fileInfosLen = fileInfos.size();
                for (int i=0; i<fileInfosLen; i++) {
                    PostImageUploadInfo imageUploadInfo =
                            new PostImageUploadInfo(
                                    postId,
                                    fileInfos.get(i).getFileName(),
                                    fileInfos.get(i).getFilePath(),
                                    i+1);
                    imageUploadInfos.add(imageUploadInfo);
                }

                postMapper.insertImages(imageUploadInfos);
            }
        }
    }
}
