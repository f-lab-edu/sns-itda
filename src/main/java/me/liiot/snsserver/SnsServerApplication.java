package me.liiot.snsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/*
@EnableAspectJAutoProxy
: AspectJ의 @Aspect 어노테이션이 명시된 오브젝트들을 다룰 수 있게 해준다.
 */

@SpringBootApplication
@EnableAspectJAutoProxy
public class SnsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnsServerApplication.class, args);
    }

}
