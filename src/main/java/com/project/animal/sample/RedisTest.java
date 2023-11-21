package com.project.animal.sample;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisTest {

    private final StringRedisTemplate template;

//    @EventListener(ApplicationReadyEvent.class)
    public void test() {
        /**
         * # Redis는 여러 자료 구조를 가지고 있고, 사용하는 구조에 따라 적절한 메소드를 사용할 수 있다.
         * - opsForValue()     : Strings를 쉽게 Serializer / Deserializer 해주는 인터페이스
         * - opsForList()      : List를 쉽게 Serializer / Deserializer 해주는 인터페이스
         * - opsForSet()       : Set을 쉽게 Serialize / Deserialize 해주는 인터페이스
         * - opsForZSet()      : ZSet을 쉽게 Serialize / Deserialize 해주는 인터페이스
         * - opsForHash()      : Hash를 쉽게 Serialize / Deserialize 해주는 인터페이스
         *
         * # RedisConfig에서 String 자료구조를 사영하였기 때문에 opsForValue() 메소드를 사용하여 데이터를 저장할 것이다.
         */

        // 1. Create 연산
        System.out.println("-----------------------------------");
        System.out.println(template.toString());
        ValueOperations<String, String> operations = template.opsForValue();
        operations.set("hello", "create");

        // 2. Read 연산
        String readData = operations.get("hello");

        System.out.println(readData);

        // 3. Update 연산
        operations.getAndSet("hello", "update");

        // 4. Delete 연산
        template.expire("hello", Duration.ZERO);
    }

    // Spring Boot를 통한 의존성 주입이 아닌 수동 테스트
    public static void main(String[] args) {
        // Redis 설정
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration("", 0);
        redisConfig.setPassword("");
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig);
        factory.afterPropertiesSet();

        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        // 스프링 부트 이용 시, 알아서 초기화 해주므로 해당 메소드는 제거해도 된다. (수동 초기화 방법)
        template.afterPropertiesSet();

        // 1. Create 연산
        ValueOperations<String, String> operations = template.opsForValue();

        operations.set("hello", "hi!!", Duration.ofMinutes(2));
    }
}