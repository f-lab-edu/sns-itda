package me.liiot.snsserver.enumeration;

import lombok.Getter;

@Getter
public enum PushType {

    FOLLOWING("FOLLOWING", "%s님이 회원님의 계정을 팔로우하기 시작했습니다.");

    private String type;
    private String content;

    PushType(String type, String content) {
        this.type = type;
        this.content = content;
    }
}
