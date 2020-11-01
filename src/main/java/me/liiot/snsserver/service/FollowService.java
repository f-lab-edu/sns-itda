package me.liiot.snsserver.service;

import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.enumeration.AlarmType;
import me.liiot.snsserver.enumeration.PushType;
import me.liiot.snsserver.mapper.FollowMapper;
import me.liiot.snsserver.model.push.PushMessage;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowMapper followMapper;

    private final AlarmService alarmService;

    private final PushService pushService;

    public void addFollowList(String userId, String followUserId) {

        followMapper.insertFollow(userId, followUserId);

        alarmService.addAlarm(userId, followUserId, AlarmType.FOLLOWING);

        PushMessage pushMessage = new PushMessage(
                PushType.FOLLOWING.getType(),
                String.format(PushType.FOLLOWING.getContent(), userId),
                Date.valueOf(LocalDate.now())
        );
        pushService.sendPushMessage(pushMessage);
    }

    public void deleteFollowList(String userId, String followUserId) {

        followMapper.deleteFollow(userId, followUserId);
    }
}
