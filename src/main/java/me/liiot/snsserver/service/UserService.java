package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.InValidValueException;
import me.liiot.snsserver.model.User;
import me.liiot.snsserver.model.UserIdAndPassword;
import me.liiot.snsserver.model.UserPasswordUpdateParam;
import me.liiot.snsserver.model.UserUpdateParam;

public interface UserService {

    public void signUpUser(User user);

    public void checkUserIdDupe(String userId);

    public User getLoginUser(UserIdAndPassword userIdAndPassword);

    public void updateUser(String userId, UserUpdateParam userUpdateParam);

    public void updateUserPassword(User user, UserPasswordUpdateParam userPasswordUpdateParam)
            throws InValidValueException;

    public void deleteUser(User user, String userPassword) throws InValidValueException;
}
