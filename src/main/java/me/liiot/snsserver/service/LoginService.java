package me.liiot.snsserver.service;

import me.liiot.snsserver.model.User;

import javax.servlet.http.HttpSession;

public interface LoginService {

    public void loginUser(User user);

    public void logoutUser();
}
