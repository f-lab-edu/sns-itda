package me.liiot.snsserver.mapper;

import me.liiot.snsserver.annotation.ClientDatabase;
import me.liiot.snsserver.model.alarm.AlarmInfo;
import me.liiot.snsserver.util.ClientDatabases;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmMapper {

    @ClientDatabase(value = ClientDatabases.MASTER)
    public void insertAlarm(AlarmInfo alarmInfo);
}
