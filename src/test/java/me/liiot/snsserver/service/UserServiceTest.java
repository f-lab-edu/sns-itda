package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.InValidValueException;
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

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.not;
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

        userService.signUpUser(testUser);

        verify(userMapper).insertUser(any(User.class));
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

        UserUpdateParam userUpdateParam = new UserUpdateParam(
                "Sarah", "01012345678", "test1@abc.com", Date.valueOf("1990-02-20")
        );

        userService.updateUser(encryptedTestUser.getUserId(), userUpdateParam);

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

        assertThrows(InValidValueException.class, () -> {
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

        assertThrows(InValidValueException.class, () -> {
            userService.deleteUser(encryptedTestUser, "5678");
        });

        verify(userMapper, times(0)).deleteUser(encryptedTestUser.getUserId());
    }
}