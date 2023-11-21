package com.project.animal.global.common.provider;

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
}
