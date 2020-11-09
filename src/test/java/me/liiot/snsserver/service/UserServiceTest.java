package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.InvalidValueException;
import me.liiot.snsserver.exception.NotUniqueIdException;
import me.liiot.snsserver.mapper.UserMapper;
import me.liiot.snsserver.model.*;
import me.liiot.snsserver.model.user.*;
import me.liiot.snsserver.util.PasswordEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class UserServiceTest {

    @Mock
    UserMapper userMapper;

    @Mock
    FileService fileService;

    @InjectMocks
    UserServiceImpl userService;

    User testUser;

    User encryptedTestUser;

    @BeforeEach
    public void setUp() {
        testUser = User.builder()
                .userId("test1")
                .password("1234")
                .name("Sally")
                .phoneNumber("01012341234")
                .email("test1@test.com")
                .birth(Date.valueOf("1990-01-10"))
                .profileMessage("안녕!")
                .profileImageName("testImage")
                .profileImagePath("C:\\Users\\cyj19\\Desktop\\Project\\sns-server\\images\\test1\\testImage")
                .build();

        encryptedTestUser = User.builder()
                .userId("test1")
                .password(PasswordEncryptor.encrypt("1234"))
                .name("Sally")
                .phoneNumber("01012341234")
                .email("test1@test.com")
                .birth(Date.valueOf("1990-01-10"))
                .profileMessage("안녕!")
                .profileImageName("testImage")
                .profileImagePath("C:\\Users\\cyj19\\Desktop\\Project\\sns-server\\images\\test1\\testImage")
                .build();
    }

    @Test
    public void signUpUserTest() {

        UserSignUpParam userSignUpParam = UserSignUpParam.builder()
                .userId("test1")
                .password("1234")
                .name("Sally")
                .phoneNumber("01012341234")
                .email("test1@test.com")
                .birth(Date.valueOf("1990-01-10"))
                .build();

        userService.signUpUser(userSignUpParam);

        verify(userMapper).insertUser(any(UserSignUpParam.class));
    }

    @Test
    public void checkUserIdDupeTestWithoutDupe() {

        when(userMapper.isExistUserId("test2")).thenReturn(false);

        assertDoesNotThrow(() -> {
            userService.checkUserIdDupe("test2");
        });

        verify(userMapper).isExistUserId("test2");
    }

    @Test
    public void checkUserIdDupeTestWithDupe() {

        when(userMapper.isExistUserId(testUser.getUserId())).thenReturn(true);

        assertThrows(NotUniqueIdException.class, () -> {
            userService.checkUserIdDupe("test1");
        });

        verify(userMapper).isExistUserId("test1");
    }

    @Test
    public void getLoginUserTestWithSuccess() {

        UserIdAndPassword userIdAndPassword = new UserIdAndPassword(testUser.getUserId(), testUser.getPassword());

        when(userMapper.getPassword(userIdAndPassword.getUserId())).thenReturn(encryptedTestUser.getPassword());
        when(userMapper.getUser(userIdAndPassword.getUserId())).thenReturn(encryptedTestUser);

        User loginUser = userService.getLoginUser(userIdAndPassword);

        verify(userMapper).getPassword("test1");
        verify(userMapper).getUser("test1");

        assertEquals(encryptedTestUser, loginUser);
    }

    @Test
    public void getLoginUserTestWithFail() {

        UserIdAndPassword userIdAndPassword = new UserIdAndPassword("test2", "5678");

        when(userMapper.getPassword(userIdAndPassword.getUserId())).thenReturn(null);

        User loginUser = userService.getLoginUser(userIdAndPassword);
        assertEquals(null, loginUser);

        verify(userMapper).getPassword("test2");
        verify(userMapper, times(0)).getUser("test2");
    }

    @Test
    public void updateUserTest() {

        MockMultipartFile testFile = new MockMultipartFile(
                "profileImage",
                "profileImage",
                "image/png",
                "profileImage".getBytes());

        UserUpdateParam userUpdateParam = UserUpdateParam.builder()
                .name("Sarah")
                .phoneNumber("01012345678")
                .email("test1@abc.com")
                .birth(Date.valueOf("1990-02-20"))
                .profileMessage("안녕하세요")
                .build();

        FileInfo fileInfo = new FileInfo("profileImage", "C:\\Users\\cyj19\\Desktop\\Project\\sns-server\\images\\test1\\profileImage");

        when(fileService.uploadFile(testFile, encryptedTestUser.getUserId())).thenReturn(fileInfo);

        userService.updateUser(encryptedTestUser, userUpdateParam, testFile);

        verify(fileService).deleteFile(encryptedTestUser.getProfileImagePath());
        verify(fileService).uploadFile(testFile, encryptedTestUser.getUserId());
        verify(userMapper).updateUser(any(UserUpdateInfo.class));
    }

    @Test
    public void updateUserPasswordTestWithSuccess() {

        UserPasswordUpdateParam userPasswordUpdateParam = new UserPasswordUpdateParam(
                "1234", "5678", "5678"
        );

        assertDoesNotThrow(() -> {
            userService.updateUserPassword(encryptedTestUser, userPasswordUpdateParam);
        });

        verify(userMapper).updateUserPassword(any(UserIdAndPassword.class));
    }

    @Test
    public void updateUserPasswordTestWithFail() {

        UserPasswordUpdateParam userPasswordUpdateParam = new UserPasswordUpdateParam(
                "5678", "1234", "6789"
        );

        assertThrows(InvalidValueException.class, () -> {
            userService.updateUserPassword(encryptedTestUser, userPasswordUpdateParam);
        });

        verify(userMapper, times(0)).updateUserPassword(any(UserIdAndPassword.class));
    }

    @Test
    public void deleteUserTestWithSuccess() {

        assertDoesNotThrow(() -> {
            userService.deleteUser(encryptedTestUser, "1234");
        });

        verify(fileService).deleteDirectory(encryptedTestUser.getUserId());
        verify(userMapper).deleteUser(encryptedTestUser.getUserId());
    }

    @Test
    public void deleteUserTestWithFail() {

        assertThrows(InvalidValueException.class, () -> {
            userService.deleteUser(encryptedTestUser, "5678");
        });

        verify(fileService, times(0)).deleteDirectory(encryptedTestUser.getUserId());
        verify(userMapper, times(0)).deleteUser(encryptedTestUser.getUserId());
    }
}