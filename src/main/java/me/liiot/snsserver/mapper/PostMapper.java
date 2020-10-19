package me.liiot.snsserver.mapper;

import me.liiot.snsserver.annotation.ClientDatabase;
import me.liiot.snsserver.model.post.Post;
import me.liiot.snsserver.model.post.PostUploadInfo;
import me.liiot.snsserver.util.ClientDatabases;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostMapper {

    @ClientDatabase(ClientDatabases.MASTER)
    void insertPost(PostUploadInfo postUploadInfo);

    @ClientDatabase(value = ClientDatabases.SLAVE)
    int getLatestPostId(String userId);

    @ClientDatabase(value = ClientDatabases.SLAVE)
    Post getPost(int postId);

    @ClientDatabase(value = ClientDatabases.SLAVE)
    List<Post> getPostsByUserId(String userId);

    @ClientDatabase(value = ClientDatabases.SLAVE)
    boolean isAuthorizedOnPost(@Param("userId") String userId, @Param("postId") int postId);

    @ClientDatabase(value = ClientDatabases.MASTER)
    void updatePost(@Param("postId") int postId, @Param("content") String content);

    @ClientDatabase(value = ClientDatabases.MASTER)
    void deletePost(int postId);
}
