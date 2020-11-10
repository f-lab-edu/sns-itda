package me.liiot.snsserver.service;

import me.liiot.snsserver.enumeration.PushType;

public interface PushService {

    public void setToken(String userId);

    public String getToken(String userId);

    public void deleteToken(String userId);

    public void sendPushMessage(String userId, String receiverId, PushType pushType, String pushMessage);
}
