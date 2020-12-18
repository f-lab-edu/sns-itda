package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.FileDeleteException;
import me.liiot.snsserver.exception.FileUploadException;
import me.liiot.snsserver.exception.InvalidValueException;
import me.liiot.snsserver.exception.NotUniqueUserIdException;
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

    @DisplayName("필수 입력 필드(사용자 ID, 비밀번호, 이름, 핸드폰 번호)가 입력된 경우 회원가입 성공")
    @Test
    public void signUpUserTestWithSuccess() {

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

    @DisplayName("아이디 중복 체크 시, 중복된 아이디가 없는 경우 예외를 던지지 않는다.")
    @Test
    public void checkUserIdDupeTestWithoutDupe() {

        when(userMapper.isExistUserId("test2")).thenReturn(false);

        assertDoesNotThrow(() -> {
            userService.checkUserIdDupe("test2");
        });

        verify(userMapper).isExistUserId("test2");
    }

    @DisplayName("아이디 중복 체크 시, 중복된 아이디가 있는 경우 NotUniqueUserIdException을 던진다.")
    @Test
    public void checkUserIdDupeTestWithDupe() {

        when(userMapper.isExistUserId(testUser.getUserId())).thenReturn(true);

        assertThrows(NotUniqueUserIdException.class, () -> {
            userService.checkUserIdDupe("test1");
        });

        verify(userMapper).isExistUserId("test1");
    }

    @DisplayName("올바른 아이디-비밀번호 쌍을 입력하는 경우 로그인 성공")
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

    @DisplayName("틀린 아이디-비밀번호 쌍을 입력하는 경우 로그인 실패")
    @Test
    public void getLoginUserTestWithFail() {

        UserIdAndPassword userIdAndPassword = new UserIdAndPassword("test2", "5678");

        when(userMapper.getPassword(userIdAndPassword.getUserId())).thenReturn(null);

        User loginUser = userService.getLoginUser(userIdAndPassword);
        assertEquals(null, loginUser);

        verify(userMapper).getPassword("test2");
        verify(userMapper, times(0)).getUser("test2");
    }

    @DisplayName("필수 입력 필드(이름, 핸드폰 번호)가 입력된 경우 회원 정보 업데이트 성공")
    @Test
    public void updateUserTestWithSuccess() {

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

    @DisplayName("기존 프로필 사진 삭제가 실패하는 경우 FileDeleteException을 던지며 회원 정보 업데이트 실패")
    @Test
    public void updateUserTestWithFail() {

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

        doThrow(FileDeleteException.class).when(fileService).deleteFile(encryptedTestUser.getProfileImagePath());

        assertThrows(FileDeleteException.class, () -> {
            userService.updateUser(encryptedTestUser, userUpdateParam, testFile);
        });

        verify(fileService).deleteFile(encryptedTestUser.getProfileImagePath());
        verify(fileService, times(0)).uploadFile(testFile, encryptedTestUser.getUserId());
        verify(userMapper, times(0)).updateUser(any(UserUpdateInfo.class));
    }

    @DisplayName("새로운 프로필 사진 업로드가 실패하는 경우 FileUploadException을 던지며 회원 정보 업데이트 실패")
    @Test
    public void updateUserTestWithFail2() {

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

        doThrow(FileUploadException.class).when(fileService).uploadFile(testFile, encryptedTestUser.getUserId());

        assertThrows(FileUploadException.class, () -> {
            userService.updateUser(encryptedTestUser, userUpdateParam, testFile);
        });

        verify(fileService).deleteFile(encryptedTestUser.getProfileImagePath());
        verify(fileService).uploadFile(testFile, encryptedTestUser.getUserId());
        verify(userMapper, times(0)).updateUser(any(UserUpdateInfo.class));
    }

    @DisplayName("세 가지 조건(올바른 현재 비밀번호, 현재 비밀번호와 다른 새 비밀번호, 새 비밀번호와 일치하는 비밀번호 확인 값)이 모두 충족되면 회원 비밀번호 변경 성공")
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

    @DisplayName("틀린 현재 비밀번호를 입력한 경우 회원 비밀번호 변경 실패")
    @Test
    public void updateUserPasswordTestWithFail() {

        UserPasswordUpdateParam userPasswordUpdateParam = new UserPasswordUpdateParam(
                "5678", "6789", "6789"
        );

        assertThrows(InvalidValueException.class, () -> {
            userService.updateUserPassword(encryptedTestUser, userPasswordUpdateParam);
        });

        verify(userMapper, times(0)).updateUserPassword(any(UserIdAndPassword.class));
    }

    @DisplayName("새로운 비밀번호가 기존 비밀번호와 같은 경우 회원 비밀번호 변경 실패")
    @Test
    public void updateUserPasswordTestWithFail2() {

        UserPasswordUpdateParam userPasswordUpdateParam = new UserPasswordUpdateParam(
                "1234", "1234", "1234"
        );

        assertThrows(InvalidValueException.class, () -> {
            userService.updateUserPassword(encryptedTestUser, userPasswordUpdateParam);
        });

        verify(userMapper, times(0)).updateUserPassword(any(UserIdAndPassword.class));
    }

    @DisplayName("새로운 비밀번호와 새로운 비밀번호 확인 값이 다른 경우 회원 비밀번호 변경 실패")
    @Test
    public void updateUserPasswordTestWithFail3() {

        UserPasswordUpdateParam userPasswordUpdateParam = new UserPasswordUpdateParam(
                "1234", "6789", "aaaa"
        );

        assertThrows(InvalidValueException.class, () -> {
            userService.updateUserPassword(encryptedTestUser, userPasswordUpdateParam);
        });

        verify(userMapper, times(0)).updateUserPassword(any(UserIdAndPassword.class));
    }

    @DisplayName("현재 비밀번호를 올바르게 입력한 경우 회원 탈퇴 성공")
    @Test
    public void deleteUserTestWithSuccess() {

        assertDoesNotThrow(() -> {
            userService.deleteUser(encryptedTestUser, "1234");
        });

        verify(fileService).deleteDirectory(encryptedTestUser.getUserId());
        verify(userMapper).deleteUser(encryptedTestUser.getUserId());
    }

    @DisplayName("틀린 비밀번호를 입력한 경우 InvalidValueException을 던지며 회원 탈퇴 실패")
    @Test
    public void deleteUserTestWithFail() {

        assertThrows(InvalidValueException.class, () -> {
            userService.deleteUser(encryptedTestUser, "5678");
        });

        verify(fileService, times(0)).deleteDirectory(encryptedTestUser.getUserId());
        verify(userMapper, times(0)).deleteUser(encryptedTestUser.getUserId());
    }
}