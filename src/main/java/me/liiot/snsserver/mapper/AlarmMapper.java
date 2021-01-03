package me.liiot.snsserver.mapper;

import me.liiot.snsserver.model.alarm.AlarmInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmMapper {

    public void insertAlarm(AlarmInfo alarmInfo);
}
