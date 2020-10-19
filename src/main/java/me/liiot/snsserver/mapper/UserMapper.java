package me.liiot.snsserver.mapper;

import me.liiot.snsserver.annotation.ClientDatabase;
import me.liiot.snsserver.model.user.User;
import me.liiot.snsserver.model.user.UserIdAndPassword;
import me.liiot.snsserver.model.user.UserSignUpParam;
import me.liiot.snsserver.model.user.UserUpdateInfo;
import me.liiot.snsserver.util.ClientDatabases;
import org.apache.ibatis.annotations.Mapper;

/*
@Mapper
: 대상 interface를 MyBatis Mapper로 등록
 */
@Mapper
public interface UserMapper {

    @ClientDatabase(value = ClientDatabases.MASTER)
    void insertUser(UserSignUpParam userSignUpParam);

    @ClientDatabase(value = ClientDatabases.SLAVE)
    boolean isExistUserId(String userId);

    @ClientDatabase(value = ClientDatabases.SLAVE)
    String getPassword(String userId);

    @ClientDatabase(value = ClientDatabases.SLAVE)
    User getUser(String UserId);

    @ClientDatabase(value = ClientDatabases.MASTER)
    void updateUser(UserUpdateInfo userUpdateInfo);

    @ClientDatabase(value = ClientDatabases.MASTER)
    void updateUserPassword(UserIdAndPassword userIdAndPassword);

    @ClientDatabase(value = ClientDatabases.MASTER)
    void deleteUser(String userId);
}
