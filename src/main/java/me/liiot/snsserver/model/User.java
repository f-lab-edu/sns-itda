package me.liiot.snsserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;

    private String userId;

    private String password;

    private String name;

    private String phoneNumber;

    private String email;

    private Date birth;
}
