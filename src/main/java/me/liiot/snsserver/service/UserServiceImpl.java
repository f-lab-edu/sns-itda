package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.NotUniqueIdException;
import me.liiot.snsserver.mapper.UserMapper;
import me.liiot.snsserver.model.User;
import me.liiot.snsserver.util.PasswordEncryptor;
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

        User encryptedUser = new User(
                user.getUserId(),
                encryptedPassword,
                user.getName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getBirth()
                );

        userMapper.insertUser(encryptedUser);
    }

    @Override
    public void checkUserIdDupe(String userId) throws NotUniqueIdException {
        boolean isExistUserId = userMapper.isExistUserId(userId);

        if (isExistUserId) {
            throw new NotUniqueIdException("중복된 아이디입니다.");
        }
    }
}
