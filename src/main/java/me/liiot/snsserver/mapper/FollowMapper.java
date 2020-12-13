package me.liiot.snsserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface FollowMapper {

    void insertFollow(@Param("userId") String userId, @Param("followUserId") String followUserId);

    void deleteFollow(@Param("userId") String userId, @Param("followUserId") String followUserId);
}
