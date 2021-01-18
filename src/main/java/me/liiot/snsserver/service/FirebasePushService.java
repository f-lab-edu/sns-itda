package me.liiot.snsserver.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liiot.snsserver.enumeration.PushType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebasePushService implements PushService {

    @Value("${firebase.account.key.path}")
    private String apiKeyPath;

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

    public void sendPushMessage(String userId, String receiverId, PushType type, String pushMessage) {

        Message message = Message.builder()
                .putData("title", type.getType())
                .putData("body", pushMessage)
                .putData("createTime", String.valueOf(LocalDateTime.now()))
                .setToken(getToken(receiverId))
                .build();

        FirebaseMessaging.getInstance().sendAsync(message);
    }

    private String getAccessToken() {
        try {
            GoogleCredential googleCredential = GoogleCredential
                    .fromStream(new FileSystemResource(apiKeyPath).getInputStream())
                    .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform",
                                                "https://www.googleapis.com/auth/firebase.messaging"));
            googleCredential.refreshToken();
            return googleCredential.getAccessToken();
        } catch (IOException e) {
            log.error("Failed getting access token: {}", e);
        }
        return null;
    }
}
