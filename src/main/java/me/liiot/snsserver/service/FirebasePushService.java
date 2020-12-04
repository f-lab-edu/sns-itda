package me.liiot.snsserver.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.enumeration.PushType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

/*
@Async
: 지정된 메소드를 비동기적으로 처리
 */
@Service
@RequiredArgsConstructor
public class FirebasePushService implements PushService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void setToken(String userId) {

        redisTemplate.opsForValue().set(userId, getAccessToken());
        redisTemplate.expire(userId, Duration.ofHours(1l));
    }

    public String getToken(String userId) {

        if (!redisTemplate.hasKey(userId)) {
            setToken(userId);
        }

        return (String) redisTemplate.opsForValue().get(userId);
    }

    public void deleteToken(String userId) {

        redisTemplate.delete(userId);
    }

    @Async(value = "taskExecutor")
    public void sendPushMessage(String userId, String receiverId, PushType type, String pushMessage) {

        Message message = Message.builder()
                .putData("title", type.getType())
                .putData("body", pushMessage)
                .putData("createTime", String.valueOf(LocalDateTime.now()))
                .setToken(getToken(receiverId))
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    private String getAccessToken() {
        try {
            GoogleCredential googleCredential = GoogleCredential
                    .fromStream(new ClassPathResource("sns-itda-firebase-adminsdk.json").getInputStream())
                    .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform",
                                                "https://www.googleapis.com/auth/firebase.messaging"));
            googleCredential.refreshToken();
            return googleCredential.getAccessToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
