package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.InvalidValueException;
import me.liiot.snsserver.model.*;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    public void signUpUser(UserSignUpParam userSignUpParam);

    public void checkUserIdDupe(String userId);

    public User getLoginUser(UserIdAndPassword userIdAndPassword);

    public User updateUser(User user, UserUpdateParam userUpdateParam, MultipartFile profileImage);

    public void updateUserPassword(User user, UserPasswordUpdateParam userPasswordUpdateParam)
            throws InvalidValueException;

    public void deleteUser(User user, String userPassword) throws InvalidValueException;
}
