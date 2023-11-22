package com.project.animal.global.common.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.animal.global.common.utils.CustomJsonHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.Optional;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class RedisServiceProvider {

    private final StringRedisTemplate template;

    private final CustomJsonHandler customJsonHandler;


    /**
     * Redis 서버에 데이터를 저장하는 메소드
     * @version 0.1
     * @param key
     * @param value
     */
    public <T> void save(String key, T value) {
        ValueOperations<String, String> operation = template.opsForValue();
        String stringValue = customJsonHandler.stringify(value);
        operation.set(key, stringValue);
    }

    /**
     * Redis 서버에 데이터를 저장하는 메소드이다.
     * 
     * @version 0.1
     * @author 박성수
     * @param key Redis Key
     * @param value Redis Value
     * @param duration Redis TimeOut
     */
    public void save(String key, String value, Duration duration) {
        ValueOperations<String, String> operation = template.opsForValue();
        operation.set(key, value, duration);
    }

    /**
     * Redis 서버에 저장된 데이터를 가져 오거나 없는 경우 디폴트를 반환하는 메소드
     *
     * @version 0.1
     * @param key
     * @return value or defaultValue
     */
    public String getOrElse(String key, String defaultValue) {
        Optional<String> value = get(key);
        return value.orElse(defaultValue);
    }

    /**
     * Redis 서버에 저장된 데이터를 가져 오는 메소드이다.
     *
     * @version 0.1
     * @author 박성수
     * @param key Redis Key
     * @return value (Redis Value)
     */
    public Optional<String> get(String key) {
        ValueOperations<String, String> operation = template.opsForValue();
        String token = operation.get(key);

        return Optional.ofNullable(token);
    }

    /**
     * Redis 서버에 있는 데이터를 삭제 하는 메소드이다.
     *
     * @version 0.1
     * @author 박성수
     * @param key Redis Key
     */
    public void remove(String key) {
        template.expire(key, Duration.ZERO);
    }


    /**
     * Redis 서버에 있는 데이터의 숫자를 1 올려주는 메소드 (동시성 보장)
     *
     * @version 0.1
     * @param key Redis Key
     */
    public void increase(String key) {
        template.opsForValue().increment(key);
    }

    /**
     * Redis 서버에 있는 데이터의 숫자를 1 내려주는 메소드 (동시성 보장)
     *
     * @version 0.1
     * @param key Redis Key
     */
    public void decrease(String key) {
        template.opsForValue().decrement(key);
    }
}
