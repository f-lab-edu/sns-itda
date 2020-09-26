package me.liiot.snsserver.service;

import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.model.User;
import me.liiot.snsserver.util.SessionKeys;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

    private final HttpSession httpSession;

    private final GenericJackson2JsonRedisSerializer serializer;

    @Override
    public void loginUser(User user) {

        httpSession.setAttribute(SessionKeys.USER, serializer.serialize(user));
    }

    @Override
    public void logoutUser() {
        httpSession.invalidate();
    }
}
