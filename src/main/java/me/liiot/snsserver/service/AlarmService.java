package me.liiot.snsserver.service;

import me.liiot.snsserver.enumeration.AlarmType;

public interface AlarmService {

    public void addAlarm(String userId, String targetId, AlarmType type);
}
