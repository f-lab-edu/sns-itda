package me.liiot.snsserver.service;

import me.liiot.snsserver.mapper.PostMapper;
import me.liiot.snsserver.model.FileInfo;
import me.liiot.snsserver.model.post.*;
import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.model.post.PostUploadInfo;
import me.liiot.snsserver.model.user.User;
import org.springframework.expression.AccessException;
import me.liiot.snsserver.util.CacheKeys;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    @Cacheable(value = CacheKeys.POST, key = "#postId")
    public Post getPost(int postId) {

        Post post = postMapper.getPost(postId);

        return post;
    }

    @Override
    public List<Post> getPostsByUser(String userId) {

        List<Post> posts = postMapper.getPostsByUserId(userId);

        return posts;
    }

    @Override
    public void updatePost(User user, int postId, String content) throws AccessException{

        boolean isAuthorizedOnPost = postMapper.isAuthorizedOnPost(user.getUserId(), postId);
        if (isAuthorizedOnPost) {
            postMapper.updatePost(postId, content);
        } else {
            throw new AccessException("해당 게시물의 수정 권한이 없습니다.");
        }
    }

    @Override
    public void deletePost(User user, int postId) throws AccessException{

        boolean isAuthorizedOnPost = postMapper.isAuthorizedOnPost(user.getUserId(), postId);
        if (isAuthorizedOnPost) {
            boolean isExistImages = fileService.isExistImages(postId);
            if (isExistImages) {
                fileService.deleteImages(postId);
            }

            postMapper.deletePost(postId);
        } else {
            throw new AccessException("해당 게시물의 삭제 권한이 없습니다.");
        }
    }
}
