package me.liiot.snsserver.service;

import me.liiot.snsserver.mapper.PostMapper;
import me.liiot.snsserver.model.FileInfo;
import me.liiot.snsserver.model.post.*;
import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.model.post.PostUploadInfo;
import me.liiot.snsserver.model.user.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.expression.AccessException;
import me.liiot.snsserver.util.CacheNames;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/*
@Cacheable(cacheNames = ..., key = ...)
: 해당 메소드의 리턴값을 캐시에 저장. 메소드가 호출될 때마다 캐시 저장소에
저장된 캐시가 있는지 확인하고 있다면 메소드를 실행하지 않고 캐시를 반환.
cacheNames - 캐시 이름
key - 캐시에 접근하기 위한 키 값
* Redis에 실제로 저장될 때는 "cacheNames::key" 형식으로 저장된다.

@CacheEvict(cacheNames = ..., key = ..., allEntries = ...)
: 지정된 키에 해당하는 캐시를 삭제
allEntries - true일 경우, 같은 이름을 가진 모든 캐시를 삭제

@Caching(evict = ..., put = ...)
: 여러 캐싱 작업을 한 번에 적용시키기 위한 어노테이션
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;

    private final FileService fileService;

    @Override
    @CacheEvict(cacheNames = CacheNames.FEED, key = "#user.userId")
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
    @Cacheable(cacheNames = CacheNames.POST, key = "#postId")
    public Post getPost(int postId) {

        Post post = postMapper.getPost(postId);

        return post;
    }

    @Override
    @Cacheable(cacheNames = CacheNames.FEED, key = "#userId")
    public List<Post> getPostsByUser(String userId) {

        List<Post> posts = postMapper.getPostsByUserId(userId);

        return posts;
    }

    @Override
    @Caching(evict = {@CacheEvict(cacheNames = CacheNames.POST, key = "#postId"),
                      @CacheEvict(cacheNames = CacheNames.FEED, key = "#user.userId")})
    public void updatePost(User user, int postId, String content) throws AccessException{

        boolean isAuthorizedOnPost = postMapper.isAuthorizedOnPost(user.getUserId(), postId);
        if (isAuthorizedOnPost) {
            postMapper.updatePost(postId, content);
        } else {
            throw new AccessException("해당 게시물의 수정 권한이 없습니다.");
        }
    }

    @Override
    @Caching(evict = {@CacheEvict(cacheNames = CacheNames.POST, key = "#postId"),
                      @CacheEvict(cacheNames = CacheNames.FEED, key = "#user.userId")})
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
