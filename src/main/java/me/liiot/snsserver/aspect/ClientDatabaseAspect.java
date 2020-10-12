package me.liiot.snsserver.aspect;

import me.liiot.snsserver.annotation.ClientDatabase;
import me.liiot.snsserver.config.tool.ClientDatabaseContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class ClientDatabaseAspect {

    @Before("@annotation(me.liiot.snsserver.annotation.ClientDatabase)")
    public void setClientDatabse(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String value = method.getAnnotation(ClientDatabase.class).value();

        ClientDatabaseContext.set(value);
    }
}
