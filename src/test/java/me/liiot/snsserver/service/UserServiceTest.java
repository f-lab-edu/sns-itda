package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.InvalidValueException;
import me.liiot.snsserver.exception.NotUniqueIdException;
import me.liiot.snsserver.mapper.UserMapper;
import me.liiot.snsserver.model.*;
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
                .build();

        encryptedTestUser = User.builder()
                .userId("test1")
                .password(PasswordEncryptor.encrypt("1234"))
                .name("Sally")
                .phoneNumber("01012341234")
                .email("test1@test.com")
                .birth(Date.valueOf("1990-01-10"))
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

        when(userMapper.getPassword(userIdAndPassword.userId)).thenReturn(encryptedTestUser.getPassword());
        when(userMapper.getUser(userIdAndPassword)).thenReturn(encryptedTestUser);

        User loginUser = userService.getLoginUser(userIdAndPassword);

        verify(userMapper).getPassword("test1");
        verify(userMapper).getUser(userIdAndPassword);

        assertEquals(encryptedTestUser, loginUser);
    }

    @Test
    public void getLoginUserTestWithFail() {

        UserIdAndPassword userIdAndPassword = new UserIdAndPassword("test2", "5678");

        when(userMapper.getPassword(userIdAndPassword.userId)).thenReturn(null);

        User loginUser = userService.getLoginUser(userIdAndPassword);
        assertEquals(null, loginUser);

        verify(userMapper).getPassword("test2");
        verify(userMapper, times(0)).getUser(userIdAndPassword);
    }

    @Test
    public void updateUserTest() {

        MockMultipartFile testFile = new MockMultipartFile("file", "orig", null, "bar".getBytes());

        UserUpdateParam userUpdateParam = UserUpdateParam.builder()
                .name("Sarah")
                .phoneNumber("01012345678")
                .email("test1@abc.com")
                .birth(Date.valueOf("1990-02-20"))
                .profileMessage("안녕하세요")
                .build();

        userService.updateUser(encryptedTestUser, userUpdateParam, testFile);

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

        verify(userMapper).deleteUser(encryptedTestUser.getUserId());
    }

    @Test
    public void deleteUserTestWithFail() {

        assertThrows(InvalidValueException.class, () -> {
            userService.deleteUser(encryptedTestUser, "5678");
        });

        verify(userMapper, times(0)).deleteUser(encryptedTestUser.getUserId());
    }
}