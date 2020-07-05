package me.liiot.snsserver.service;

import me.liiot.snsserver.mapper.UserMapper;
import me.liiot.snsserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
@Service
: "모델에 독립된 인터페이스로 제공되는 작업"으로 정의된 서비스임을 명시
  비즈니스 로직을 처리할 클래스
*/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void signUpUser(User user) {

        userMapper.insertUser(user);
    }

    @Override
    public int checkIdDupe(String userId) {

        return userMapper.checkIdDupe(userId);
    }
}
