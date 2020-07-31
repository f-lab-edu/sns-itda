package me.liiot.snsserver.service;

import me.liiot.snsserver.model.User;
import me.liiot.snsserver.model.UserLoginInfo;
import me.liiot.snsserver.model.UserPasswordUpdateParam;
import me.liiot.snsserver.model.UserUpdateParam;

public interface UserService {

    public void signUpUser(User user);

    public void checkUserIdDupe(String userId);

    public User getLoginUser(UserLoginInfo userLoginInfo);

    public void updateUser(String userId, UserUpdateParam userUpdateParam);

    public void updateUserPassword(User user, UserPasswordUpdateParam userPasswordUpdateParam);
}
