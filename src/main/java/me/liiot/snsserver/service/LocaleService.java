package me.liiot.snsserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LocaleService {

    private final LocaleResolver localeResolver;

    private final HttpServletRequest request;

    public Locale getCurrentUserLocale() {

        return localeResolver.resolveLocale(request);
    }
}
