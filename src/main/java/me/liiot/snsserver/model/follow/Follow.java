package me.liiot.snsserver.model.follow;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;

@Getter
@AllArgsConstructor
public class Follow {

    private final int id;

    private final String userId;

    private final String followUserId;

    private final Date startFollow;
}
