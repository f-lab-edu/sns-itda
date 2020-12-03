package me.liiot.snsserver.model.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.liiot.snsserver.enumeration.AlarmType;

@Getter
@AllArgsConstructor
public class AlarmInfo {

    private final String userId;

    private final String targetId;

    private final AlarmType type;
}
