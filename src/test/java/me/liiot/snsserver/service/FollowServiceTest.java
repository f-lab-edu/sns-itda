package me.liiot.snsserver.service;

import me.liiot.snsserver.enumeration.AlarmType;
import me.liiot.snsserver.enumeration.PushType;
import me.liiot.snsserver.mapper.FollowMapper;
import me.liiot.snsserver.model.user.User;
import me.liiot.snsserver.util.PasswordEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.sql.Date;
import java.util.Locale;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    private FollowMapper followMapper;

    @Mock
    private AlarmService alarmService;

    @Mock
    private PushService pushService;

    @Mock
    private LocaleService localeService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private FollowService followService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userId("test2")
                .password(PasswordEncryptor.encrypt("1234"))
                .name("Joy")
                .phoneNumber("01012345678")
                .email("test2@test.com")
                .birth(Date.valueOf("1990-11-01"))
                .build();
    }

    @DisplayName("팔로우 / 팔로우 리스트 추가")
    @Test
    void addFollowListTest() {

        String pushMessage = "test2님이 회원님을 팔로우하기 시작했습니다.";

        doNothing().when(alarmService).addAlarm("test2", "test1", AlarmType.FOLLOWING);
        when(localeService.getCurrentUserLocale()).thenReturn(Locale.KOREA);
        when(messageSource.getMessage(eq("push.message.following"),
                eq(new String[]{"test2"}),
                any(Locale.class)))
            .thenReturn(pushMessage);
        doNothing().when(pushService).sendPushMessage("test2", "test1", PushType.FOLLOWING, pushMessage);

        followService.addFollowList(testUser.getUserId(), "test1");

        verify(followMapper).insertFollow("test2", "test1");
        verify(alarmService).addAlarm("test2", "test1", AlarmType.FOLLOWING);
        verify(messageSource).getMessage("push.message.following", new String[]{"test2"}, Locale.KOREA);
        verify(localeService).getCurrentUserLocale();
    }

    @DisplayName("언팔로우 / 팔로우 리스트 삭제")
    @Test
    void deleteFollowListTest() {

        followService.deleteFollowList(testUser.getUserId(), "test1");

        verify(followMapper).deleteFollow("test2", "test1");
    }
}