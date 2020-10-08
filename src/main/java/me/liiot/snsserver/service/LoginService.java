package me.liiot.snsserver.service;

public interface LoginService {

    public void loginUser(String userId);

    public void logoutUser();

    public String getCurrentUserId();
}
