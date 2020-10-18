package me.liiot.snsserver.mapper;

import me.liiot.snsserver.annotation.ClientDatabase;
import me.liiot.snsserver.util.ClientDatabases;
import org.springframework.data.repository.query.Param;

public interface FollowMapper {

    @ClientDatabase(value = ClientDatabases.MASTER)
    void insertFollow(@Param("userId") String userId, @Param("targetId") String targetId);

    @ClientDatabase(value = ClientDatabases.MASTER)
    void deleteFollow(@Param("userId") String userId, @Param("targetId") String targetId);
}
