package me.liiot.snsserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserIdAndPassword {

    private final String userId;

    private final String password;
}
