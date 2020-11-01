package me.liiot.snsserver.service;

import me.liiot.snsserver.model.push.PushMessage;

public interface PushService {

    public void sendPushMessage(PushMessage pushMessage);
}
