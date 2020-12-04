package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.InvalidValueException;
import me.liiot.snsserver.exception.NotUniqueIdException;
import me.liiot.snsserver.mapper.UserMapper;
import me.liiot.snsserver.model.*;
import me.liiot.snsserver.model.user.*;
import me.liiot.snsserver.util.PasswordEncryptor;
import me.liiot.snsserver.util.SessionKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
@DisplayName(value = ...)
: 지정된 테스트 클래스나 테스트 메소드를 사용자가 정의한 이름으로 선언

ArgumentCaptor<T>
: 메서드에 전달되는 지정된 타입의 인수를 캡처
- 캡처된 인수는 검증 로직에서 사용
 */
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

    @DisplayName("회원가입")
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

        ArgumentCaptor<UserSignUpParam> valueCapture = ArgumentCaptor.forClass(UserSignUpParam.class);
        doNothing().when(userMapper).insertUser(valueCapture.capture());

        userService.signUpUser(userSignUpParam);

        verify(userMapper).insertUser(any(UserSignUpParam.class));
        assertEquals(true, PasswordEncryptor.isMatch(userSignUpParam.getPassword(), valueCapture.getValue().getPassword()));
    }

    @DisplayName("아이디 중복 체크 시, 중복된 아이디가 없는 경우")
    @Test
    public void checkUserIdDupeTestWithoutDupe() {

        when(userMapper.isExistUserId("test2")).thenReturn(false);

        assertDoesNotThrow(() -> {
            userService.checkUserIdDupe("test2");
        });

        verify(userMapper).isExistUserId("test2");
    }

    @DisplayName("아이디 중복 체크 시, 중복된 아이디가 있는 경우")
    @Test
    public void checkUserIdDupeTestWithDupe() {

        when(userMapper.isExistUserId(testUser.getUserId())).thenReturn(true);

        assertThrows(NotUniqueIdException.class, () -> {
            userService.checkUserIdDupe("test1");
        });

        verify(userMapper).isExistUserId("test1");
    }

    @DisplayName("로그인 성공")
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

    @DisplayName("로그인 실패")
    @Test
    public void getLoginUserTestWithFail() {

        UserIdAndPassword userIdAndPassword = new UserIdAndPassword("test2", "5678");

        when(userMapper.getPassword(userIdAndPassword.getUserId())).thenReturn(null);

        User loginUser = userService.getLoginUser(userIdAndPassword);
        assertEquals(null, loginUser);

        verify(userMapper).getPassword("test2");
        verify(userMapper, times(0)).getUser("test2");
    }

    @DisplayName("회원 정보 업데이트")
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

    @DisplayName("회원 비밀번호 변경 성공")
    @Test
    public void updateUserPasswordTestWithSuccess() {

        UserPasswordUpdateParam userPasswordUpdateParam = new UserPasswordUpdateParam(
                "1234", "5678", "5678"
        );

        ArgumentCaptor<UserIdAndPassword> valueCapture = ArgumentCaptor.forClass(UserIdAndPassword.class);
        doNothing().when(userMapper).updateUserPassword(valueCapture.capture());

        assertDoesNotThrow(() -> {
            userService.updateUserPassword(encryptedTestUser, userPasswordUpdateParam);
        });

        verify(userMapper).updateUserPassword(any(UserIdAndPassword.class));
        assertEquals(true,
                PasswordEncryptor.isMatch(userPasswordUpdateParam.getNewPassword(),
                                          valueCapture.getValue().getPassword()));
    }

    @DisplayName("회원 비밀번호 변경 실패")
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

    @DisplayName("회원 탈퇴 성공")
    @Test
    public void deleteUserTestWithSuccess() {

        assertDoesNotThrow(() -> {
            userService.deleteUser(encryptedTestUser, "1234");
        });

        verify(fileService).deleteDirectory(encryptedTestUser.getUserId());
        verify(userMapper).deleteUser(encryptedTestUser.getUserId());
    }

    @DisplayName("회원 탈퇴 실패")
    @Test
    public void deleteUserTestWithFail() {

        assertThrows(InvalidValueException.class, () -> {
            userService.deleteUser(encryptedTestUser, "5678");
        });

        verify(fileService, times(0)).deleteDirectory(encryptedTestUser.getUserId());
        verify(userMapper, times(0)).deleteUser(encryptedTestUser.getUserId());
    }
}