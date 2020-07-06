package me.liiot.snsserver.service;

import me.liiot.snsserver.model.User;

public interface UserService {

    public void signUpUser(User user);

    public void checkIdDupe(String userId);
}
