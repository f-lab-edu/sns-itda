package me.liiot.snsserver.model.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Post {

    private final String name;

    private final String phoneNumber;

    private final String email;

    private final String profileMessage;
}
