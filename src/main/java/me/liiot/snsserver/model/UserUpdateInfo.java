package me.liiot.snsserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Date;

@Getter
@Builder
@AllArgsConstructor
public class UserUpdateInfo {

    private final String userId;

    private final String name;

    private final String phoneNumber;

    private final String email;

    private final Date birth;
}
