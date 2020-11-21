package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.AlreadyLoginException;
import me.liiot.snsserver.model.user.User;
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
import org.springframework.mock.web.MockHttpSession;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionLoginServiceTest {

    @Mock
    private MockHttpSession mockHttpSession;

    @InjectMocks
    private SessionLoginService sessionLoginService;

    private User testUser;

    @BeforeEach
    void setUpEach() {

        testUser = User.builder()
                .userId("test1")
                .password(PasswordEncryptor.encrypt("1234"))
                .name("Sarah")
                .phoneNumber("01012345678")
                .email("test1@test.com")
                .birth(Date.valueOf("1990-04-13"))
                .build();
    }

    @DisplayName("로그인 성공")
    @Test
    void loginUserTestWithSuccess() {

        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(mockHttpSession).setAttribute(eq(SessionKeys.USER_ID), valueCapture.capture());

        sessionLoginService.loginUser(testUser.getUserId());

        verify(mockHttpSession).setAttribute(SessionKeys.USER_ID, testUser.getUserId());
        assertEquals(testUser.getUserId(), valueCapture.getValue());
    }

    @DisplayName("이미 로그인이 되어 있는 경우 AlreadyLoginException을 던지며 로그인 실패")
    @Test
    void loginUserTestWithFail() {

        mockHttpSession.setAttribute(SessionKeys.USER_ID, testUser);

        assertThrows(AlreadyLoginException.class, () -> {
            sessionLoginService.loginUser(testUser.getUserId());
        });

        verify(mockHttpSession).setAttribute(SessionKeys.USER_ID, testUser.getUserId());
    }

    @DisplayName("로그아웃 성공")
    @Test
    void logoutUserTestWithSuccess() {

        mockHttpSession.setAttribute(SessionKeys.USER_ID, testUser);

        sessionLoginService.logoutUser();

        verify(mockHttpSession).invalidate();
        assertNull(mockHttpSession.getAttribute(SessionKeys.USER_ID));
    }

    @DisplayName("로그인이 되어 있지 않은 경우에도 로그아웃 성공")
    @Test
    void logoutUserTestWithSuccess2() {

        sessionLoginService.logoutUser();

        verify(mockHttpSession).invalidate();
        assertNull(mockHttpSession.getAttribute(SessionKeys.USER_ID));
    }
}