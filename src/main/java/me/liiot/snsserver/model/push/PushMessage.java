package me.liiot.snsserver.model.push;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public class PushMessage {

    private final String token;

    public final String type;

    public final String content;

    public final Date createTime;

    public PushMessage(String type, String content, Date createTime) {
        this.token = getAccessToken();
        this.type = type;
        this.content = content;
        this.createTime = createTime;
    }

    private static String getAccessToken() {
        try {
            GoogleCredential googleCredential = GoogleCredential
                    .fromStream(new ClassPathResource("sns-itda-firebase-adminsdk.json").getInputStream())
                    .createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase",
                                                "https://www.googleapis.com/auth/cloud-platform",
                                                "https://www.googleapis.com/auth/firebase.readonly",
                                                "https://www.googleapis.com/auth/firebase.messaging"));
            googleCredential.refreshToken();
            return googleCredential.getAccessToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
