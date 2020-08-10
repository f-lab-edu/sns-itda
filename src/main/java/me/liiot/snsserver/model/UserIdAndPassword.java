package me.liiot.snsserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserIdAndPassword {

    public final String userId;

    public final String password;
}
