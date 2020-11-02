package me.liiot.snsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/*
@EnableAspectJAutoProxy
: AspectJ의 @Aspect 어노테이션이 명시된 오브젝트들을 다룰 수 있게 해준다.
 */
@EnableAsync
@EnableCaching
@EnableAspectJAutoProxy
@SpringBootApplication
public class SnsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnsServerApplication.class, args);
    }

}
