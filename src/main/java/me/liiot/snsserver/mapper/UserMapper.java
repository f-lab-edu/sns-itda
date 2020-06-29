package me.liiot.snsserver.mapper;

import me.liiot.snsserver.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void insertUser(User user);

    int checkIdDupe(String userId);
}
