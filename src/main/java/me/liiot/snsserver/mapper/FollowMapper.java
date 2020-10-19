package me.liiot.snsserver.mapper;

import me.liiot.snsserver.annotation.ClientDatabase;
import me.liiot.snsserver.model.follow.Follow;
import me.liiot.snsserver.util.ClientDatabases;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface FollowMapper {

    @ClientDatabase(value = ClientDatabases.MASTER)
    void insertFollow(@Param("userId") String userId, @Param("targetId") String targetId);

    @ClientDatabase(value = ClientDatabases.MASTER)
    void deleteFollow(@Param("userId") String userId, @Param("targetId") String targetId);

    @ClientDatabase(value = ClientDatabases.SLAVE)
    List<Follow> getFollowList(String userId);

    @ClientDatabase(value = ClientDatabases.SLAVE)
    List<Follow> getFollowingList(String followUserId);
}
