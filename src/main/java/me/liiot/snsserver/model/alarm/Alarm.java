package me.liiot.snsserver.model.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.liiot.snsserver.enumeration.AlarmType;

import java.sql.Date;

@Getter
@Builder
@AllArgsConstructor
public class Alarm {

    private final int id;

    private final String userId;

    private final String targetId;

    private final AlarmType type;

    private final boolean readed;

    private final Date createTime;
}
