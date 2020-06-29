package me.liiot.snsserver.service;

import me.liiot.snsserver.mapper.UserMapper;
import me.liiot.snsserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
