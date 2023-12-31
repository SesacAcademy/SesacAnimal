package com.project.animal.global.common.provider;

import com.project.animal.global.common.constant.AuthType;
import com.project.animal.global.common.dto.MailDto;
import com.project.animal.global.common.provider.inf.AuthCodeProvider;
import com.project.animal.member.exception.InvalidCodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.Random;
import static com.project.animal.global.common.constant.ExpirationTime.REDIS_MAIL_AUTHCODE_TIMEOUT;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailAuthCodeProvider implements AuthCodeProvider {

    @Value("${spring.mail.authcode.digit}")
    private int authCodeDigit;

    private final Random rd = new Random();

    private final RedisServiceProvider redisServiceProvider;

    private final MailServiceProvider mailServiceProvider;

    /**
     * 이메일 인증 번호를 생성하고 Redis 서버에 저장한 다음, 사용자에게 인증 번호 메일을 발송하는 메소드이다.
     *
     * @version 0.1
     * @author 박성수
     * @param email 유저 이메일
     * @throws MailSendException 메일 발송에 실패할 시, 예외 발생
     */
    public void generateAuthCode(String email) {
        // Redis에 저장할 Key 생성 (ex. MAIL:test@naver.com)
        String key = createKey(email);

        // Redis에 저장할 Value 생성 (인증 번호 6자리)
        String value = createValue();

        // Redis에 인증 번호 저장
        redisServiceProvider.save(key, value, Duration.ofSeconds(REDIS_MAIL_AUTHCODE_TIMEOUT));

        log.info("이메일 인증 번호 발급");
        log.info("Key : {}", key);
        log.info("Value : {}", value);

        // MailDto 객체 생성
        MailDto mail = MailDto.builder()
                        .title("[새싹 애니멀] 이메일 인증 번호입니다.")
                        .content("인증 번호 : " + value)
                        .build();

        // 인증 번호 메일 발송
        mailServiceProvider.sendMail(email, mail);
    }

    /**
     * 사용자에게서 입력 받은 인증 번호가 Redis 서버에 저장된 인증 번호와 일치하는지 비교하는 메소드이다.
     *
     * @version 0.1
     * @author 박성수
     * @param email 유저 이메일
     * @param authCode 이메일 인증 번호
     * @return Boolean (인증 번호가 유효하면 true, 유효하지 않으면 false 리턴)
     * @throws InvalidCodeException 인증 번호가 만료되거나 인증 번호 발급할 때 사용한 이메일이 아닌 경우, 해당 예외 발생
     */
    public boolean validateAuthCode(String email, String authCode) {
        // Key 생성
        String key = createKey(email);

        // 인증 번호가 만료되거나 이메일 인증 번호 발급 시, 사용한 이메일이 아닌 경우, 해당 예외 발생
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
     * @param email 유저 이메일
     * @return String (Redis Key)
     */
    private String createKey(String email) {
        return AuthType.MAIL + ":" + email;
    }

    /**
     * 이메일 인증 번호를 생성하는 메소드이다.
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
