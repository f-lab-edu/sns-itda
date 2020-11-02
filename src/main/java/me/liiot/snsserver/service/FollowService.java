package me.liiot.snsserver.service;

import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.enumeration.AlarmType;
import me.liiot.snsserver.enumeration.PushType;
import me.liiot.snsserver.mapper.FollowMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowMapper followMapper;

    private final AlarmService alarmService;

    private final PushService pushService;

    public void addFollowList(String userId, String followUserId) {

        followMapper.insertFollow(userId, followUserId);

        alarmService.addAlarm(userId, followUserId, AlarmType.FOLLOWING);

        pushService.sendPushMessage(userId, followUserId, PushType.FOLLOWING);
    }

    public void deleteFollowList(String userId, String followUserId) {

        followMapper.deleteFollow(userId, followUserId);
    }
}
