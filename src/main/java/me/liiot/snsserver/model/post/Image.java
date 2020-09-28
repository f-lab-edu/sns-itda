package me.liiot.snsserver.model.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Image {

    private final int id;

    private final int postId;

    private final String imageName;

    private final String imagePath;

    private final int seq;
}
