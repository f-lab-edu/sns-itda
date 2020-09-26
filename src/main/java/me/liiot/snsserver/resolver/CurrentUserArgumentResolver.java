package me.liiot.snsserver.resolver;

import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.annotation.CurrentUser;
import me.liiot.snsserver.model.User;
import me.liiot.snsserver.util.SessionKeys;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final GenericJackson2JsonRedisSerializer serializer;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {

        try {
            byte[] serializedSource = (byte[]) nativeWebRequest.getAttribute(SessionKeys.USER, WebRequest.SCOPE_SESSION);
            User user = serializer.deserialize(serializedSource, User.class);

            return user;
        } catch (IllegalArgumentException e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }
}
