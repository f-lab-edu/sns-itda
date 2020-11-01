package me.liiot.snsserver.service;

import me.liiot.snsserver.enumeration.AlarmType;
import me.liiot.snsserver.mapper.AlarmMapper;
import me.liiot.snsserver.model.alarm.AlarmInfo;
import me.liiot.snsserver.model.user.User;
import me.liiot.snsserver.util.PasswordEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AlarmServiceTest {

    @Mock
    private AlarmMapper alarmMapper;

    @InjectMocks
    private AlarmService alarmService;

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

    @DisplayName("알림 추가")
    @Test
    void addAlarmTest() {

        alarmService.addAlarm(testUser.getUserId(), "test1", AlarmType.FOLLOWING);

        verify(alarmMapper).insertAlarm(any(AlarmInfo.class));
    }
}