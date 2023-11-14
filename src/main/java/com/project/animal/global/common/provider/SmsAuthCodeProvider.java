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

    /**
     * 문자 인증 번호를 생성하고 Redis 서버에 저장한 다음, 사용자에게 인증 번호 문자를 발송하는 메소드이다.
     *
     * @version 0.1
     * @author 박성수
     * @param phone 유저 휴대폰 번호
     */
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

        // 인증 번호 문자 발송
        smsServiceProvider.sendSms(phone,content);
    }

    /**
     * 사용자에게서 입력 받은 인증 번호가 Redis 서버에 저장된 인증 번호와 일치하는지 비교하는 메소드이다.
     * 
     * @version 0.1
     * @author 박성수
     * @param phone 유저 휴대폰 번호
     * @param authCode 문자 인증 번호
     * @return Boolean (인증 번호가 유효하면 true, 유효하지 않으면 false 리턴)
     */
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

    /**
     * Redis 서버에 인증 번호를 저장할 때, 사용할 key를 생성하는 메소드이다.
     *
     * @version 0.1
     * @author 박성수
     * @param phone
     * @return String (Redis Key)
     */
    private String createKey(String phone) {
        return AuthType.PHONE + ":" + phone;
    }

    /**
     * 문자 인증 번호를 생성하는 메소드이다.
     *
     * @version 0.1
     * @author 박성수
     * @return String (AuthCode)
     */
    private String createValue() {
        String value = "";

        for (int i=0; i<authCodeDigit; i++) {
            value += rd.nextInt(10);
        }
        return value;
    }
}
