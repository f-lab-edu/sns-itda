package me.liiot.snsserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.liiot.snsserver.util.PasswordEncryptor;

import java.sql.Date;

@Getter
@AllArgsConstructor
public class UserLoginInfo {

    public final String userId;

    public final String password;
}
