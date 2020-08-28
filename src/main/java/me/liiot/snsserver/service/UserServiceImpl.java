package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.InValidValueException;
import me.liiot.snsserver.exception.NotUniqueIdException;
import me.liiot.snsserver.mapper.UserMapper;
import me.liiot.snsserver.model.*;
import me.liiot.snsserver.util.PasswordEncryptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
@Service
: "모델에 독립된 인터페이스로 제공되는 작업"으로 정의된 서비스임을 명시
  비즈니스 로직을 처리할 클래스
*/
@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void signUpUser(User user) {

        String encryptedPassword = PasswordEncryptor.encrypt(user.getPassword());

        User encryptedUser = User.builder()
                .userId(user.getUserId())
                .password(encryptedPassword)
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .birth(user.getBirth())
                .build();

        userMapper.insertUser(encryptedUser);
    }

    @Override
    public void checkUserIdDupe(String userId) throws NotUniqueIdException {
        boolean isExistUserId = userMapper.isExistUserId(userId);

        if (isExistUserId) {
            throw new NotUniqueIdException("중복된 아이디입니다.");
        }
    }

    @Override
    public User getLoginUser(UserIdAndPassword userIdAndPassword) {

        String storedPassword = userMapper.getPassword(userIdAndPassword.getUserId());

        boolean isValidPassword = PasswordEncryptor.isMatch(userIdAndPassword.getPassword(), storedPassword);

        if (storedPassword == null || !isValidPassword) {
            return null;
        }

        User user = userMapper.getUser(userIdAndPassword);
        return user;
    }

    @Override
    public void updateUser(String currentUserId,
                           UserUpdateParam userUpdateParam) {

        UserUpdateInfo userUpdateInfo = UserUpdateInfo.builder()
                .userId(currentUserId)
                .name(userUpdateParam.getName())
                .phoneNumber(userUpdateParam.getPhoneNumber())
                .email(userUpdateParam.getEmail())
                .birth(userUpdateParam.getBirth())
                .build();

        userMapper.updateUser(userUpdateInfo);
    }

    @Override
    public void updateUserPassword(User currentUser,
                                   UserPasswordUpdateParam userPasswordUpdateParam)
            throws InValidValueException {

        String currentUserId = currentUser.getUserId();
        String currentUserPassword = currentUser.getPassword();

        boolean isValidPassword = PasswordEncryptor.isMatch(
                userPasswordUpdateParam.getExistPassword(),
                currentUserPassword
        );

        if (!isValidPassword ||
            StringUtils.equals(userPasswordUpdateParam.getExistPassword(), userPasswordUpdateParam.getNewPassword()) ||
            !(StringUtils.equals(userPasswordUpdateParam.getNewPassword(), userPasswordUpdateParam.getCheckNewPassword()))) {
            throw new InValidValueException("올바르지 않은 값입니다. 다시 입력해주세요.");
        }

        String encryptedPassword = PasswordEncryptor.encrypt(userPasswordUpdateParam.getNewPassword());
        UserIdAndPassword userIdAndPassword = new UserIdAndPassword(currentUserId, encryptedPassword);

        userMapper.updateUserPassword(userIdAndPassword);
    }

    @Override
    public void deleteUser(User currentUser, String inputPassword) throws InValidValueException {
        String currentUserID = currentUser.getUserId();
        String currentUserPassword = currentUser.getPassword();

        boolean isValidPassword = PasswordEncryptor.isMatch(inputPassword, currentUserPassword);

        if (!isValidPassword) {
            throw new InValidValueException();
        }

        userMapper.deleteUser(currentUserID);
    }
}
