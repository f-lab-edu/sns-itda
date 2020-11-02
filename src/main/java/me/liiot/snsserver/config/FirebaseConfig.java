package me.liiot.snsserver.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/*
@PostConstruct
: 빈의 초기화를 위해 모든 의존성이 주입이 된 후에 실행됨
 */
@Configuration
public class FirebaseConfig {

    @Value("${firebase.account.key.path}")
    private String apiKeyPath;

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount = new ClassPathResource(apiKeyPath).getInputStream();

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
