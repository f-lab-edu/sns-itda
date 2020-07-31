package me.liiot.snsserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;

@Getter
@AllArgsConstructor
public class UserUpdateParam {

    private final String name;

    private final String phoneNumber;

    private final String email;

    private final Date birth;
}
