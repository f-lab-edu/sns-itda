package me.liiot.snsserver.model;

import lombok.Getter;
import me.liiot.snsserver.util.PasswordEncryptor;

import java.sql.Date;

/*
@Getter
: 각 필드에 대한 접근자 메소드를 생성
 */
@Getter
public class User {

    private final String userId;

    private final String password;

    private final String name;

    private final String phoneNumber;

    private final String email;

    private final Date birth;

    public User(String userId, String password, String name,
                String phoneNumber, String email, Date birth) {

        this.userId = userId;
        this.password = PasswordEncryptor.encrypt(password);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birth = birth;
    }
}
