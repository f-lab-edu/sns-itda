package me.liiot.snsserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/*
@Data
: @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode을 한꺼번에 설정

@AllArgsConstructor
: 모든 필드 값을 파라미터로 받는 생성자 생성

@NoArgsConstructor
: 파라미터가 없는 기본 생성자를 생성
 */
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
