package me.liiot.snsserver.aspect;

import me.liiot.snsserver.model.User;
import me.liiot.snsserver.util.SessionKeys;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpSession;

@Component
@Aspect
public class CheckLoginAspect {

    @Autowired
    HttpSession httpSession;

    @Before("@annotation(me.liiot.snsserver.annotation.CheckLogin)")
    public void checkLogin() throws HttpClientErrorException {

        User currentUser = (User)httpSession.getAttribute(SessionKeys.USER);

        if (currentUser == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }
}
