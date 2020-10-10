package me.liiot.snsserver.model.user;

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

    private final String profileMessage;

    private final String profileImageName;

    private final String profileImagePath;
}
