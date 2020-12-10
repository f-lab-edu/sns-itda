package me.liiot.snsserver.mapper;

import me.liiot.snsserver.annotation.ClientDatabase;
import me.liiot.snsserver.util.ClientDatabases;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface FollowMapper {

    @ClientDatabase(value = ClientDatabases.MASTER)
    void insertFollow(@Param("userId") String userId, @Param("followUserId") String followUserId);

    @ClientDatabase(value = ClientDatabases.MASTER)
    void deleteFollow(@Param("userId") String userId, @Param("followUserId") String followUserId);
}
