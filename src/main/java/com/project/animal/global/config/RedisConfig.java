package com.project.animal.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String hostName;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    public RedisConnectionFactory createRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(hostName, port);
        config.setPassword(password);
        RedisConnectionFactory factory = new LettuceConnectionFactory(config);

        return factory;
    }

    @Bean
    @Primary
    public RedisTemplate<String, String> createStringRedisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(createRedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }

    @Bean
    public RedisTemplate<String, Object> createJsonRedisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(createRedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());

        return template;
    }
}

/**
 * 1. RedisConnection는 바이너리 데이터로 다루기 때문에 높은 추상화를 제공하는 RedisTemplate을 사용하자!
 *
 * 2. Spring Boot에서는 Spring Data Redis를 통해 Lettuce, Jedis 라는 두 가지 오픈소스 자바 라이브러리를 사용할 수 있다.
 *
 * 3. Lettuce는 별도의 설정 없이 사용할 수 있으며 Jedis를 사용하고자 하면 별도의 의존성을 필요로 한다.
 *
 * 4. Spring Data Redis는 Redis에 대한 두 가지 접근 방식을 제공한다.
 *
 * 5. 하나는 RedisTemplate을 이용한 방식이며, 다른 하나는 RedisRepository를 이용한 방식이고 두 방식 모두 Redis에 접근하기
 *    위해서는 Redis 저장소와 연결하는 과정이 필요하다.
 *
 * [참고]
 * - https://growth-coder.tistory.com/228
 * - https://wildeveloperetrain.tistory.com/32
 * - https://luvstudy.tistory.com/143
 */