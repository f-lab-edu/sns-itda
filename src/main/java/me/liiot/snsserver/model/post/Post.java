package me.liiot.snsserver.model.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Date;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Post {

    private final int id;

    private final String userId;

    private final String content;

    private final Date createTime;

    private List<Image> images;

    public Post(int id, String userId, String content, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.createTime = createTime;
    }
}
