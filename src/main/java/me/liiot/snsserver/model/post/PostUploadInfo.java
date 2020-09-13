package me.liiot.snsserver.model.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUploadInfo {

    private String userId;

    private String content;
}
