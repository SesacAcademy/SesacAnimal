package com.project.animal.global.common.provider;

import com.project.animal.member.exception.InvalidTokenException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Optional;
import static com.project.animal.global.common.constant.ExpirationTime.REDIS_MAIL_TOKEN_TIMEOUT;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class RedisServiceProvider {

    private final RedisTemplate<String, String> template;

    /**
     * Redis DB에 데이터를 저장하는 메소드
     * 
     * @version 0.1
     * @author 박성수
     * @param key
     * @param value
     * @param duration
     */
    public void save(String key, String value, @Nullable Duration duration) {
        ValueOperations<String, String> operation = template.opsForValue();
        operation.set(key, value, Duration.ofSeconds(REDIS_MAIL_TOKEN_TIMEOUT));
    }

    /**
     * Redis DB에 저장된 데이터를 가져 오는 메소드
     *
     * @version 0.1
     * @author 박성수
     * @param key (Redis Key)
     * @return value (Redis Value)
     */
    public Optional<String> get(String key) {
        ValueOperations<String, String> operation = template.opsForValue();
        String token = operation.get(key);

        return Optional.ofNullable(token);
    }

    /**
     * Redis DB에 있는 데이터를 삭제 하는 메소드
     *
     * @version 0.1
     * @author 박성수
     * @param key (Redis Key)
     */
    public void remove(String key) {
        template.expire(key, Duration.ZERO);
    }
}
