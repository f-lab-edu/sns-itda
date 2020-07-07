package me.liiot.snsserver.mapper;

import me.liiot.snsserver.model.User;
import me.liiot.snsserver.model.UserLoginInfo;
import org.apache.ibatis.annotations.Mapper;

/*
@Mapper
: 대상 interface를 MyBatis Mapper로 등록
 */
@Mapper
public interface UserMapper {

    void insertUser(User user);

    boolean checkIdDupe(String userId);

    String getPassword(String userId);

    User getUser(UserLoginInfo userLoginInfo);
}
