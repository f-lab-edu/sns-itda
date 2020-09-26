package me.liiot.snsserver.aspect;

import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.model.User;
import me.liiot.snsserver.util.SessionKeys;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpSession;

@Component
@Aspect
@RequiredArgsConstructor
public class CheckLoginAspect {

    private final HttpSession httpSession;

    private final GenericJackson2JsonRedisSerializer serializer;

    @Before("@annotation(me.liiot.snsserver.annotation.CheckLogin)")
    public void checkLogin() throws HttpClientErrorException {

        byte[] serializedSource = (byte[]) httpSession.getAttribute(SessionKeys.USER);
        User currentUser = serializer.deserialize(serializedSource, User.class);

        if (currentUser == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }
}
