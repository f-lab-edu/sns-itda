package me.liiot.snsserver.aspect;

import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.service.LoginService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
@Aspect
@RequiredArgsConstructor
public class CheckLoginAspect {

    private final LoginService loginService;

    @Before("@annotation(me.liiot.snsserver.annotation.CheckLogin)")
    public void checkLogin() throws HttpClientErrorException {

        String currentUserId = loginService.getCurrentUserId();

        if (currentUserId == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }
}
