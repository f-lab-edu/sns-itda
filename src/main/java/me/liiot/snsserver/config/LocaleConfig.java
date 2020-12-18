package me.liiot.snsserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/*
LocaleResolver
: 웹 요청으로부터 Locale 정보 검색 및 수정 기능을 제공하는 인터페이스
- 대표적인 구현체: AcceptHeaderLocaleResolver, CookieLocaleResolver, SessionLocaleResolver 등

CookieLocaleResolver
: 쿠키를 통해 Locale 정보를 관리하는 LocaleResolver 구현체
- setCookieName: 웹 요청으로부터 전달 받은 Locale 정보를 저장할 쿠키 이름

LocaleChangeInterceptor
: 웹 요청을 가로채어 지정된 파라미터를 기반으로 Locale을 전환하는 인터셉터
- setParamName: Locale 정보를 체크 할 파라미터 지정

동작 원리: 웹 요청의 URL에서 LocaleChangeInterceptor에 지정한 파라미터 값이 감지되면
         이를 전달 받아 쿠키에 Locale 정보를 저장하고, 그 이후부터 쿠키에 담긴 값을 가져와 사용
 */
@Configuration
public class LocaleConfig {

    @Bean
    public LocaleResolver localeResolver() {

        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setDefaultLocale(Locale.KOREA);
        localeResolver.setCookieName("lang");

        return localeResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {

        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");

        return interceptor;
    }
}
