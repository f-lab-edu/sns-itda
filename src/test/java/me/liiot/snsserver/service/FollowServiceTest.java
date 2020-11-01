package me.liiot.snsserver.service;

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

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    private FollowMapper followMapper;

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

        followService.addFollowList(testUser.getUserId(), "test1");

        verify(followMapper).insertFollow("test2", "test1");
    }

    @DisplayName("언팔로우 / 팔로우 리스트 삭제")
    @Test
    void deleteFollowListTest() {

        followService.deleteFollowList(testUser.getUserId(), "test1");

        verify(followMapper).deleteFollow("test2", "test1");
    }
}