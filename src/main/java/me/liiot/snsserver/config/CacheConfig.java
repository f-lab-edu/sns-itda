package me.liiot.snsserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/*
RedisCacheManager
: Redis Cache 인스턴스를 관리하고 저장소에서 캐시를 검색하는데 사용되는 엔티티

RedisCacheConfiguration
: RedisCacheManager에 관련 옵션을 설정하기 위한 오브젝트
disableCachingNullValues - null 값이 캐싱될 수 없도록 설정.
entryTtl - 캐시의 만료 시간을 설정.
serializeValuesWith - 캐시 Value를 직렬화-역직렬화 하는데 사용하는 Pair 지정
* Value는 다양한 자료구조가 올 수 있기 때문에 GenericJackson2JsonRedisSerializer를 사용
* Key Serializer 기본 값이 StringRedisSerializer로 지정되어 있어 serializeKeysWith 생략
 */
@Configuration
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration redisCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofHours(1))
                .disableCachingNullValues();

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(redisCacheConfig)
                .build();

        return redisCacheManager;
    }
}
