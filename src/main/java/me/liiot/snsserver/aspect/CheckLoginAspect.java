package me.liiot.snsserver.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.model.User;
import me.liiot.snsserver.util.SessionKeys;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpSession;

@Component
@Aspect
@RequiredArgsConstructor
public class CheckLoginAspect {

    private final HttpSession httpSession;

    private final ObjectMapper mapper;

    @Before("@annotation(me.liiot.snsserver.annotation.CheckLogin)")
    public void checkLogin() throws HttpClientErrorException {

        User currentUser = null;

        try {
            String jsonStr = (String) httpSession.getAttribute(SessionKeys.USER);
            currentUser = mapper.readValue(jsonStr, User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("로그인 인증을 하는 과정에서 에러가 발생하였습니다.", e);
        }

        if (currentUser == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }
}
