package me.liiot.snsserver.config;

import me.liiot.snsserver.util.CacheNames;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/*
RedisCacheManager
: Redis Cache 인스턴스를 관리하고 저장소에서 캐시를 검색하는데 사용되는 엔티티

RedisCacheConfiguration
: RedisCacheManager에 관련 옵션을 설정하기 위한 오브젝트
-entryTtl - 캐시의 만료 시간을 설정.
-serializeValuesWith - 캐시 Value를 직렬화-역직렬화 하는데 사용하는 Pair 지정
* Value는 다양한 자료구조가 올 수 있기 때문에 GenericJackson2JsonRedisSerializer를 사용
* Key Serializer 기본 값이 StringRedisSerializer로 지정되어 있어 serializeKeysWith 생략
-withInitialCacheConfigurations - 여러 개의 CacheConfiguration을 설정할 때 사용.
* post의 경우, 리소스의 변경이 거의 일어나지 않으므로 만료 시간을 1시간으로 지정
* feed의 경우, post의 생성, 수정, 삭제 등으로 리소스의 변경이 자주 일어나므로 만료 시간을 5초로 지정
 */
@Configuration
public class CacheConfig {

    @Value("${spring.redis.cache.host}")
    private String hostName;

    @Value("${spring.redis.cache.port}")
    private int port;

    @Bean(name = "redisCacheConnectionFactory")
    RedisConnectionFactory redisCacheConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(hostName);
        redisStandaloneConfiguration.setPort(port);
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisCacheManager cacheManager(@Qualifier("redisCacheConnectionFactory") RedisConnectionFactory connectionFactory) {

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        Map<String, RedisCacheConfiguration> redisCacheConfigMap = new HashMap<>();
        redisCacheConfigMap.put(CacheNames.POST, defaultConfig.entryTtl(Duration.ofHours(1)));
        redisCacheConfigMap.put(CacheNames.FEED, defaultConfig.entryTtl(Duration.ofSeconds(5L)));

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
                .withInitialCacheConfigurations(redisCacheConfigMap)
                .build();

        return redisCacheManager;
    }
}
