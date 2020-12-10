package me.liiot.snsserver.service;

import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.enumeration.AlarmType;
import me.liiot.snsserver.enumeration.PushType;
import me.liiot.snsserver.mapper.FollowMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowMapper followMapper;

    private final AlarmService alarmService;

    private final PushService pushService;

    private final LocaleService localeService;

    private final MessageSource messageSource;

    public void addFollowList(String userId, String followUserId) {

        followMapper.insertFollow(userId, followUserId);

        alarmService.addAlarm(userId, followUserId, AlarmType.FOLLOWING);

        String pushMessage = messageSource.getMessage(
                "push.message.following",
                new String[]{userId},
                localeService.getCurrentUserLocale());
        pushService.sendPushMessage(userId, followUserId, PushType.FOLLOWING, pushMessage);
    }

    public void deleteFollowList(String userId, String followUserId) {

        followMapper.deleteFollow(userId, followUserId);
    }
}
