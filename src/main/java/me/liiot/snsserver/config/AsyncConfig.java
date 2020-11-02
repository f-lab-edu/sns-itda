package me.liiot.snsserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/*
비동기 처리를 위한 별도의 스레드풀 생성
: @Async 어노테이션을 사용하면 Default로 SimpleAsyncTaskExecutor가 생성되어 사용된다.
그러나 이는 어떤 스레드도 재사용하지 않고 호출마다 새로운 스레드를 생성하기 때문에 요청이 많은
경우, 성능이 저하된다. 따라서, 새로운 스레드 풀을 생성하고 이를 프로젝트 특성에 맞게 튜닝한 후
빈으로 등록한다.

corePoolsize: 기본적으로 실행을 대기하고 있는 스레드 수
maxPoolsize: 최대로 생성할 수 있는 스레드 수
queueCapacity: 요청받은 작업을 저장할 최대 갯수(이 크기보다 많은 작업이 큐에 들어와야 poolSize가 증가한다.)
threadNamePrefix: spring이 생성하는 스레드의 접두사
 */
@Configuration
public class AsyncConfig {

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(300);
        executor.setThreadNamePrefix("fcmTask-");
        executor.initialize();
        return executor;
    }
}
