package me.liiot.snsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableCaching
@EnableAspectJAutoProxy
@SpringBootApplication
public class SnsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnsServerApplication.class, args);
    }

}
