package com.project.animal.global.common.provider;

import com.project.animal.global.common.constant.AuthType;
import com.project.animal.global.common.provider.inf.AuthCodeProvider;
import com.project.animal.member.exception.InvalidCodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.Random;

import static com.project.animal.global.common.constant.ExpirationTime.REDIS_SMS_AUTHCODE_TIMEOUT;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsAuthCodeProvider implements AuthCodeProvider {

    @Value("${spring.sms.authcode.digit}")
    private int authCodeDigit;

    private final SmsServiceProvider smsServiceProvider;

    private final Random rd = new Random();

    private final RedisServiceProvider redisServiceProvider;

    @Override
    public void generateAuthCode(String phone) {
        // Redis에 저장할 Key 생성 (ex. PHONE:010-1234-1234)
        String key = createKey(phone);

        // Redis에 저장할 Value 생성 (인증 번호 6자리)
        String value = createValue();

        // Redis에 인증 번호 저장
        redisServiceProvider.save(key, value, Duration.ofSeconds(REDIS_SMS_AUTHCODE_TIMEOUT));

        log.info("문자 인증 번호 발급");
        log.info("Key : {}", key);
        log.info("Value : {}", value);

        String content = "[새싹 애니멀] 문자 인증 번호입니다. \n" +
                         "인증 번호 : " + value;

        smsServiceProvider.sendSms(phone,content);
    }

    @Override
    public boolean validateAuthCode(String phone, String authCode) {
        // Key 생성
        String key = createKey(phone);

        // 인증 번호가 만료되거나 문자 인증 번호 발급 시, 사용한 휴대폰 번호가 아닌 경우, 해당 예외 발생
        String findAuthCode = redisServiceProvider.get(key).orElseThrow(() -> {
            throw new InvalidCodeException("유효하지 않은 인증 번호입니다.");
        });

        // 사용자가 보내온 인증 번호 값과 서버에 저장된 인증 번호 값을 비교
        return findAuthCode.equals(authCode);
    }

    private String createKey(String phone) {
        return AuthType.PHONE + ":" + phone;
    }

    private String createValue() {
        String value = "";

        for (int i=0; i<authCodeDigit; i++) {
            value += rd.nextInt(10);
        }
        return value;
    }
}
