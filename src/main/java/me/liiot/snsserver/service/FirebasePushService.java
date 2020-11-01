package me.liiot.snsserver.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import me.liiot.snsserver.model.push.PushMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class FirebasePushService implements PushService {

    @Async(value = "taskExecutor")
    public void sendPushMessage(PushMessage pushMessage) {

        Message message = Message.builder()
                .putData("title", pushMessage.getType())
                .putData("body", pushMessage.getContent())
                .putData("createTime", String.valueOf(pushMessage.getCreateTime()))
                .setToken(pushMessage.getToken())
                .build();

        FirebaseMessaging.getInstance().sendAsync(message);
    }
}
