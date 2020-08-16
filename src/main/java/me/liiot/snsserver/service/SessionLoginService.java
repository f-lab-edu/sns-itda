package me.liiot.snsserver.service;

import me.liiot.snsserver.model.User;
import me.liiot.snsserver.service.LoginService;
import me.liiot.snsserver.util.SessionKeys;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class SessionLoginService implements LoginService {

    @Override
    public void loginUser(User user) {
        HttpSession httpSession = getSession();
        httpSession.setAttribute(SessionKeys.USER, user);
    }

    @Override
    public void logoutUser() {
        HttpSession httpSession = getSession();
        httpSession.invalidate();
    }

    private HttpSession getSession() {
        HttpServletRequest servletRequest =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return servletRequest.getSession();
    }
}
