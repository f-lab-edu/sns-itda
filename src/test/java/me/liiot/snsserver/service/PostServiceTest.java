package me.liiot.snsserver.service;

import me.liiot.snsserver.mapper.PostMapper;
import me.liiot.snsserver.exception.FileDeleteException;
import me.liiot.snsserver.exception.NotExistUserIdException;
import me.liiot.snsserver.mapper.UserMapper;
import me.liiot.snsserver.model.FileInfo;
import me.liiot.snsserver.model.post.Post;
import me.liiot.snsserver.model.post.PostUploadInfo;
import me.liiot.snsserver.model.user.User;
import me.liiot.snsserver.util.PasswordEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.expression.AccessException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostMapper postMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private FileService fileService;

    @InjectMocks
    private PostServiceImpl postService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userId("test1")
                .password(PasswordEncryptor.encrypt("1234"))
                .name("Sarah")
                .phoneNumber("01012345678")
                .email("test1@test.com")
                .birth(Date.valueOf("1990-03-03"))
                .build();
    }

    @DisplayName("이미지가 없는 게시글 업로드")
    @Test
    void uploadPostTestWithoutImages() {

        List<MultipartFile> images = new ArrayList<>();

        postService.uploadPost(testUser, "content", images);

        verify(postMapper).insertPost(any(PostUploadInfo.class));

        verify(fileService, times(0)).uploadFile(any(MultipartFile.class), any(String.class));
        verify(fileService, times(0)).uploadImage(any(Integer.class), any(FileInfo.class));

        verify(fileService, times(0)).uploadFiles(images, testUser.getUserId());
        verify(fileService, times(0)).uploadImages(any(Integer.class), any(List.class));
    }

    @DisplayName("이미지가 1개인 게시글 업로드")
    @Test
    void uploadPostTestWithoutOneImage() {

        MockMultipartFile testFile = new MockMultipartFile(
                "postImage",
                "postImage",
                "image/png",
                "postImage".getBytes());
        List<MultipartFile> images = new ArrayList<>();
        images.add(testFile);

        FileInfo testFileInfo = new FileInfo("image1", "path1");

        when(postMapper.getLatestPostId(testUser.getUserId())).thenReturn(8);
        when(fileService.uploadFile(testFile, testUser.getUserId())).thenReturn(testFileInfo);

        postService.uploadPost(testUser, "content", images);

        verify(postMapper).insertPost(any(PostUploadInfo.class));

        verify(fileService).uploadFile(testFile, testUser.getUserId());
        verify(fileService).uploadImage(8, testFileInfo);

        verify(fileService, times(0)).uploadFiles(images, testUser.getUserId());
        verify(fileService, times(0)).uploadImages(any(Integer.class), any(List.class));
    }

    @DisplayName("이미지가 2개 이상인 게시글 업로드")
    @Test
    void uploadPostTestWithoutSomeImages() {

        MockMultipartFile testFile1 = new MockMultipartFile(
                "postImage1",
                "postImage1",
                "image/png",
                "postImage1".getBytes());
        MockMultipartFile testFile2 = new MockMultipartFile(
                "postImage2",
                "postImage2",
                "image/png",
                "postImage2".getBytes());
        List<MultipartFile> images = new ArrayList<>();
        images.add(testFile1);
        images.add(testFile2);

        FileInfo testFileInfo1 = new FileInfo("image1", "path1");
        FileInfo testFileInfo2 = new FileInfo("image2", "path2");
        List<FileInfo> fileInfos = new ArrayList<>();
        fileInfos.add(testFileInfo1);
        fileInfos.add(testFileInfo2);

        when(postMapper.getLatestPostId(testUser.getUserId())).thenReturn(9);
        when(fileService.uploadFiles(images, testUser.getUserId())).thenReturn(fileInfos);

        postService.uploadPost(testUser, "content", images);

        verify(postMapper).insertPost(any(PostUploadInfo.class));

        verify(fileService, times(0)).uploadFile(any(MultipartFile.class), any(String.class));
        verify(fileService, times(0)).uploadImage(any(Integer.class), any(FileInfo.class));

        verify(fileService).uploadFiles(images, testUser.getUserId());
        verify(fileService).uploadImages(9, fileInfos);
    }

    @DisplayName("현재 존재하는 게시물의 id를 통해 게시물 조회")
    @Test
    void getPostTest() {

        Post testPost = new Post(8, "test1", "content", Date.valueOf("2020-10-16"));

        when(postMapper.getPost(8)).thenReturn(testPost);

        Post targetPost = postService.getPost(8);

        assertEquals(targetPost, testPost);
        verify(postMapper).getPost(8);
    }

    @DisplayName("현재 존재하는 사용자의 id를 통해 피드 조회")
    @Test
    void getPostsByUserTestWithSuccess() {
        Post testPost1 = new Post(8, "test2", "content", Date.valueOf("2020-10-16"));
        Post testPost2 = new Post(9, "test2", "content", Date.valueOf("2020-10-16"));
        List<Post> posts = new ArrayList<>();
        posts.add(testPost1);
        posts.add(testPost2);

        when(userMapper.isExistUserId("test2")).thenReturn(true);
        when(postMapper.getPostsByUserId("test2")).thenReturn(posts);

        List<Post> targetPosts = postService.getPostsByUser("test2");

        assertEquals(targetPosts, posts);
        verify(userMapper).isExistUserId("test2");
        verify(postMapper).getPostsByUserId("test2");
    }

    @DisplayName("존재하지 않는 사용자의 피드를 요청한 경우 NotExistUserIdException을 던지며 피드 조회 실패")
    @Test
    void getPostsByUserTestWithFail() {

        when(userMapper.isExistUserId("test100")).thenReturn(false);

        assertThrows(NotExistUserIdException.class, () -> {
            postService.getPostsByUser("test100");
        });

        verify(userMapper).isExistUserId("test100");
        verify(postMapper, times(0)).getPostsByUserId("test100");
    }

    @DisplayName("자신이 작성한 게시물인 경우 게시물 수정 성공")
    @Test
    void updatePostTestWithSuccess() {

        when(postMapper.isAuthorizedOnPost(testUser.getUserId(), 8)).thenReturn(true);

        assertDoesNotThrow(() -> {
            postService.updatePost(testUser, 8, "update content");
        });

        verify(postMapper).isAuthorizedOnPost(testUser.getUserId(), 8);
        verify(postMapper).updatePost(8, "update content");
    }

    @DisplayName("다른 사용자가 작성한 게시물인 경우 AccessException을 던지며 게시물 수정 실패")
    @Test
    void updatePostTestWithFail() {

        when(postMapper.isAuthorizedOnPost(testUser.getUserId(), 9)).thenReturn(false);

        assertThrows(AccessException.class, () -> {
            postService.updatePost(testUser, 9, "update content");
        });

        verify(postMapper).isAuthorizedOnPost(testUser.getUserId(), 9);
        verify(postMapper, times(0)).updatePost(9, "update content");
    }

    @DisplayName("자신이 작성하고 이미지가 없는 게시물 삭제")
    @Test
    void deletePostTestWithSuccess1() {

        when(postMapper.isAuthorizedOnPost(testUser.getUserId(), 8)).thenReturn(true);
        when(fileService.isExistImages(8)).thenReturn(false);

        assertDoesNotThrow(() -> {
            postService.deletePost(testUser, 8);
        });

        verify(postMapper).isAuthorizedOnPost(testUser.getUserId(), 8);
        verify(fileService).isExistImages(8);
        verify(fileService, times(0)).deleteImages(8);
        verify(postMapper).deletePost(8);
    }

    @DisplayName("자신이 작성하고 이미지가 있는 게시물 삭제")
    @Test
    void deletePostTestWithSuccess2() {

        when(postMapper.isAuthorizedOnPost(testUser.getUserId(), 10)).thenReturn(true);
        when(fileService.isExistImages(10)).thenReturn(true);

        assertDoesNotThrow(() -> {
            postService.deletePost(testUser, 10);
        });

        verify(postMapper).isAuthorizedOnPost(testUser.getUserId(), 10);
        verify(fileService).isExistImages(10);
        verify(fileService).deleteImages(10);
        verify(postMapper).deletePost(10);
    }

    @DisplayName("게시물 이미지 파일 삭제가 실패한 경우 FileDeleteException을 던지며 게시물 삭제 실패")
    @Test
    void deletePostTestWithFail() {

        when(postMapper.isAuthorizedOnPost(testUser.getUserId(), 11)).thenReturn(true);
        when(fileService.isExistImages(11)).thenReturn(true);
        doThrow(FileDeleteException.class).when(fileService).deleteImages(11);

        assertThrows(FileDeleteException.class, () -> {
            postService.deletePost(testUser, 11);
        });

        verify(postMapper).isAuthorizedOnPost(testUser.getUserId(), 11);
        verify(fileService).isExistImages(11);
        verify(fileService).deleteImages(11);
        verify(postMapper, times(0)).deletePost(11);
    }

    @DisplayName("다른 사람이 작성한 게시물인 경우 AccessException을 던지며 게시물 삭제 실패")
    @Test
    void deletePostTestWithFail2() {

        when(postMapper.isAuthorizedOnPost(testUser.getUserId(), 9)).thenReturn(false);

        assertThrows(AccessException.class, () -> {
            postService.deletePost(testUser, 9);
        });

        verify(postMapper).isAuthorizedOnPost(testUser.getUserId(), 9);
        verify(fileService, times(0)).isExistImages(9);
        verify(fileService, times(0)).deleteImages(9);
        verify(postMapper, times(0)).deletePost(9);
    }
}