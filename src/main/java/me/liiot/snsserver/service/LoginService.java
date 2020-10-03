package me.liiot.snsserver.service;

import me.liiot.snsserver.model.User;

public interface LoginService {

    public void loginUser(User user);

    public void logoutUser();

    public String getCurrentUserId();
}
