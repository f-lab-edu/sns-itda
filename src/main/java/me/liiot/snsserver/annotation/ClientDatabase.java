package me.liiot.snsserver.annotation;

import me.liiot.snsserver.util.ClientDatabases;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientDatabase {
    String value() default ClientDatabases.MASTER;
}
