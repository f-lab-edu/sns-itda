package me.liiot.snsserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.model.User;
import me.liiot.snsserver.util.SessionKeys;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

    private final HttpSession httpSession;

    private final ObjectMapper mapper;

    @Override
    public void loginUser(User user) {

        try {
            String jsonStr = mapper.writeValueAsString(user);
            httpSession.setAttribute(SessionKeys.USER, jsonStr);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("로그인을 하는 과정에서 에러가 발생하였습니다.", e);
        }
    }

    @Override
    public void logoutUser() {
        httpSession.invalidate();
    }
}
