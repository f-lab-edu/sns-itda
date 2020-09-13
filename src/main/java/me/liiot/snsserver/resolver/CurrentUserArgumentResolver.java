package me.liiot.snsserver.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.liiot.snsserver.annotation.CurrentUser;
import me.liiot.snsserver.model.user.User;
import me.liiot.snsserver.util.SessionKeys;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        HttpSession httpSession = servletRequest.getSession();

        ObjectMapper mapper = new ObjectMapper();

        String jsonStr = (String)httpSession.getAttribute(SessionKeys.USER);
        User user = mapper.readValue(jsonStr, User.class);

        return user;
    }
}
