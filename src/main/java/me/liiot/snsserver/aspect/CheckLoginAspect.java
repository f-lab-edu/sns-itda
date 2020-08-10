package me.liiot.snsserver.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
@Aspect
public class CheckLoginAspect {
    @Before("@annotation(me.liiot.snsserver.annotation.CheckLogin)")
    public void checkLogin() throws HttpClientErrorException {
        HttpServletRequest servletRequest =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession httpSession = servletRequest.getSession();

        if (httpSession.getAttribute("user") == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }
}
