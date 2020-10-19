package me.liiot.snsserver.service;

import me.liiot.snsserver.mapper.FollowMapper;
import me.liiot.snsserver.model.follow.Follow;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    private FollowMapper followMapper;

    @InjectMocks
    private FollowServiceImpl followService;

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

    @DisplayName("팔로우 리스트 조회")
    @Test
    void getFollowListTest() {
        Follow follow1 = new Follow(1, "test2", "test1", Date.valueOf("2020-10-18"));
        Follow follow2 = new Follow(2, "test2", "test3", Date.valueOf("2020-10-18"));
        List<Follow> followList = new ArrayList<>();
        followList.add(follow1);
        followList.add(follow2);

        when(followMapper.getFollowList("test2")).thenReturn(followList);

        List<Follow> result = followService.getFollowList(testUser.getUserId());

        verify(followMapper).getFollowList("test2");
        assertEquals(followList, result);
    }

    @DisplayName("팔로잉 리스트 조회")
    @Test
    void getFollowingListTest() {
        Follow follow1 = new Follow(3, "test3", "test2", Date.valueOf("2020-10-18"));
        Follow follow2 = new Follow(4, "test4", "test2", Date.valueOf("2020-10-18"));
        List<Follow> followingList = new ArrayList<>();
        followingList.add(follow1);
        followingList.add(follow2);

        when(followMapper.getFollowingList("test2")).thenReturn(followingList);

        List<Follow> result = followService.getFollowingList(testUser.getUserId());

        verify(followMapper).getFollowingList("test2");
        assertEquals(followingList, result);
    }
}