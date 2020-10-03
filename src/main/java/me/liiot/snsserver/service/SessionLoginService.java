package me.liiot.snsserver.service;

import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.model.User;
import me.liiot.snsserver.util.SessionKeys;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

    private final HttpSession httpSession;

    @Override
    public void loginUser(User user) {

        httpSession.setAttribute(SessionKeys.USER_ID, user.getUserId());
    }

    @Override
    public void logoutUser() {
        httpSession.invalidate();
    }

    @Override
    public String getCurrentUserId() {

        return (String) httpSession.getAttribute(SessionKeys.USER_ID);
    }
}
