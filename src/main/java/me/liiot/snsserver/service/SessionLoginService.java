package me.liiot.snsserver.service;

import me.liiot.snsserver.model.user.User;
import me.liiot.snsserver.util.SessionKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;

@Service
public class SessionLoginService implements LoginService {

    @Autowired
    HttpSession httpSession;

    @Override
    public void loginUser(User user) {
        httpSession.setAttribute(SessionKeys.USER, user);
    }

    @Override
    public void logoutUser() {
        httpSession.invalidate();
    }
}
