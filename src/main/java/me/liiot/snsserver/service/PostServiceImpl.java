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

        Post post = postMapper.getPost(postId);

        Post postWithImages = toPostWithImages(post);

        if (postWithImages != null) return postWithImages;
        else return post;
    }

    @Override
    public List<Post> getPostsByUser(String userId) {

        List<Post> posts = postMapper.getPostsByUserId(userId);

        for (Post post : posts) {
            Post postWithImages = toPostWithImages(post);

            if (postWithImages != null) {
                posts.set(posts.indexOf(post), postWithImages);
            }
        }
        return posts;
    }

    private Post toPostWithImages(Post post) {

        boolean isExistImages = fileService.isExistImages(post.getId());
        if (isExistImages) {
            List<Image> images = fileService.getImages(post.getId());

            return Post.builder()
                    .id(post.getId())
                    .userId(post.getUserId())
                    .content(post.getContent())
                    .createTime(post.getCreateTime())
                    .images(images)
                    .build();
        }
        return null;
    }
}
