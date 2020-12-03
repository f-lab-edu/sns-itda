package me.liiot.snsserver.service;

import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.exception.AlreadyLoginException;
import me.liiot.snsserver.util.SessionKeys;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

    private final HttpSession httpSession;

    private final PushService pushService;

    @Override
    public void loginUser(String userId) throws AlreadyLoginException {

        if (getCurrentUserId() != null) {
            throw new AlreadyLoginException();
        }

        httpSession.setAttribute(SessionKeys.USER_ID, userId);

        pushService.setToken(userId);
    }

    @Override
    public void logoutUser() {

        pushService.deleteToken(getCurrentUserId());

        httpSession.invalidate();
    }

    @Override
    public String getCurrentUserId() {

        return (String) httpSession.getAttribute(SessionKeys.USER_ID);
    }
}
