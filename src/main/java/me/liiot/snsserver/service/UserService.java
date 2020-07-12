package me.liiot.snsserver.service;

import me.liiot.snsserver.model.User;
import me.liiot.snsserver.model.UserLoginInfo;

public interface UserService {

    public void signUpUser(User user);

    public void checkUserIdDupe(String userId);

    public User loginUser(UserLoginInfo userLoginInfo);
}
