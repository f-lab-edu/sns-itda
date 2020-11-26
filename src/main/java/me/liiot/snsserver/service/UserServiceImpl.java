package me.liiot.snsserver.service;

import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.exception.InvalidValueException;
import me.liiot.snsserver.exception.NotUniqueIdException;
import me.liiot.snsserver.mapper.UserMapper;
import me.liiot.snsserver.model.*;
import me.liiot.snsserver.model.user.*;
import me.liiot.snsserver.util.CacheNames;
import me.liiot.snsserver.util.PasswordEncryptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/*
@Service
: "모델에 독립된 인터페이스로 제공되는 작업"으로 정의된 서비스임을 명시
  비즈니스 로직을 처리할 클래스
*/
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final FileService fileService;

    @Override
    public void signUpUser(UserSignUpParam userSignUpParam) {

        String encryptedPassword = PasswordEncryptor.encrypt(userSignUpParam.getPassword());

        UserSignUpParam encryptedParam = UserSignUpParam.builder()
                .userId(userSignUpParam.getUserId())
                .password(encryptedPassword)
                .name(userSignUpParam.getName())
                .phoneNumber(userSignUpParam.getPhoneNumber())
                .email(userSignUpParam.getEmail())
                .birth(userSignUpParam.getBirth())
                .build();

        userMapper.insertUser(encryptedParam);
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
        if (storedPassword == null) {
            return null;
        }

        boolean isValidPassword = PasswordEncryptor.isMatch(userIdAndPassword.getPassword(), storedPassword);
        if (!isValidPassword) {
            return null;
        }

        User user = userMapper.getUser(userIdAndPassword.getUserId());
        return user;
    }

    @Override
    public void updateUser(User currentUser,
                           UserUpdateParam userUpdateParam,
                           MultipartFile profileImage) {

        fileService.deleteFile(currentUser.getProfileImagePath());

        FileInfo fileInfo = fileService.uploadFile(profileImage, currentUser.getUserId());

        UserUpdateInfo userUpdateInfo = UserUpdateInfo.builder()
                .userId(currentUser.getUserId())
                .name(userUpdateParam.getName())
                .phoneNumber(userUpdateParam.getPhoneNumber())
                .email(userUpdateParam.getEmail())
                .birth(userUpdateParam.getBirth())
                .profileMessage(userUpdateParam.getProfileMessage())
                .profileImageName(fileInfo.getFileName())
                .profileImagePath(fileInfo.getFilePath())
                .build();

        userMapper.updateUser(userUpdateInfo);
    }

    @Override
    public void updateUserPassword(User currentUser,
                                   UserPasswordUpdateParam userPasswordUpdateParam)
            throws InvalidValueException {

        boolean isValidPassword = PasswordEncryptor.isMatch(
                userPasswordUpdateParam.getExistPassword(),
                currentUser.getPassword()
        );

        if (!isValidPassword ||
                StringUtils.equals(userPasswordUpdateParam.getExistPassword(), userPasswordUpdateParam.getNewPassword()) ||
                !(StringUtils.equals(userPasswordUpdateParam.getNewPassword(), userPasswordUpdateParam.getCheckNewPassword()))) {
            throw new InvalidValueException("올바르지 않은 값입니다. 다시 입력해주세요.");
        }

        String encryptedPassword = PasswordEncryptor.encrypt(userPasswordUpdateParam.getNewPassword());
        UserIdAndPassword userIdAndPassword = new UserIdAndPassword(currentUser.getUserId(), encryptedPassword);

        userMapper.updateUserPassword(userIdAndPassword);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.FEED, key = "#currentUser.userId")
    public void deleteUser(User currentUser, String inputPassword) throws InvalidValueException {

        boolean isValidPassword = PasswordEncryptor.isMatch(inputPassword, currentUser.getPassword());

        if (!isValidPassword) {
            throw new InvalidValueException();
        }

        fileService.deleteDirectory(currentUser.getUserId());
        userMapper.deleteUser(currentUser.getUserId());
    }
}
