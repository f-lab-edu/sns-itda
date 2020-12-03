package me.liiot.snsserver.service;

import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.enumeration.AlarmType;
import me.liiot.snsserver.mapper.AlarmMapper;
import me.liiot.snsserver.model.alarm.AlarmInfo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmMapper alarmMapper;

    public void addAlarm(String userId, String targetId, AlarmType type) {

        AlarmInfo alarmInfo = new AlarmInfo(userId, targetId, type);

        alarmMapper.insertAlarm(alarmInfo);
    }
}
