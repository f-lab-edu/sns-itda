package me.liiot.snsserver.service;

import me.liiot.snsserver.exception.AlreadyLoginException;

public interface LoginService {

    public void loginUser(String userId) throws AlreadyLoginException;

    public void logoutUser();

    public String getCurrentUserId();
}
