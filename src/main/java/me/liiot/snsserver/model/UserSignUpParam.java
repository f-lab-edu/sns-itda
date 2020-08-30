package me.liiot.snsserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Date;

@Getter
@Builder
@AllArgsConstructor
public class UserSignUpParam {

    private final String userId;

    private final String password;

    private final String name;

    private final String phoneNumber;

    private final String email;

    private final Date birth;
}
