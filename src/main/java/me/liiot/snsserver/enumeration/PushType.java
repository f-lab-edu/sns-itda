package me.liiot.snsserver.enumeration;

import lombok.Getter;

@Getter
public enum PushType {

    FOLLOWING("FOLLOWING");

    private String type;

    PushType(String type) {
        this.type = type;
    }
}
