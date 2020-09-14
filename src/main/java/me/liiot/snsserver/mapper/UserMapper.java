package me.liiot.snsserver.mapper;

import me.liiot.snsserver.model.User;
import me.liiot.snsserver.model.UserIdAndPassword;
import me.liiot.snsserver.model.UserSignUpParam;
import me.liiot.snsserver.model.UserUpdateInfo;
import org.apache.ibatis.annotations.Mapper;

/*
@Mapper
: 대상 interface를 MyBatis Mapper로 등록
 */
@Mapper
public interface UserMapper {

    void insertUser(UserSignUpParam userSignUpParam);

    boolean isExistUserId(String userId);

    String getPassword(String userId);

    User getUser(String UserId);

    void updateUser(UserUpdateInfo userUpdateInfo);

    void updateUserPassword(UserIdAndPassword userIdAndPassword);

    void deleteUser(String userId);
}
