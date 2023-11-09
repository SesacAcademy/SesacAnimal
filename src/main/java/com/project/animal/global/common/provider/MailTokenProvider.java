package com.project.animal.global.common.provider;

import com.project.animal.global.common.constant.AuthType;
import com.project.animal.global.common.dto.MailDto;
import com.project.animal.member.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.Random;

import static com.project.animal.global.common.constant.ExpirationTime.REDIS_MAIL_TOKEN_TIMEOUT;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailTokenProvider {

    @Value("${spring.mail.token.digit}")
    private int tokenDigit;

    private final Random rd = new Random();

    private final RedisServiceProvider redisServiceProvider;

    private final MailServiceProvider mailServiceProvider;

    /**
     * 이메일 인증번호를 생성하고 Redis에 저장한 다음, 사용자에게 인증번호를 발송하는 메소드
     *
     * @version 0.1
     * @author 박성수
     * @param email (유저 이메일)
     * @throws MailSendException - 메일 발송에 실패할 시, 예외 발생
     */
    public void createToken(String email) {
        // Redis에 저장할 Key 생성 (ex. MAIL:test@naver.com)
        String key = createKey(email);

        // Redis에 저장할 Value 생성 (토큰 : 인증번호 6자리)
        String value = createValue();

        // Redis에 토큰 저장
        redisServiceProvider.save(key, value, Duration.ofSeconds(REDIS_MAIL_TOKEN_TIMEOUT));

        log.info("이메일 토큰 발급");
        log.info("Key : {}", key);
        log.info("Value : {}", value);

        // MailDto 객체 생성
        MailDto mail = MailDto.builder()
                        .title("[새싹 애니멀] 이메일 인증 번호입니다.")
                        .content("인증번호 : " + value)
                        .build();

        // 인증번호 메일 발송
        mailServiceProvider.sendMail(email, mail);
    }

    /**
     * 사용자에게서 입력받은 인증번호가 서버에 저장된 인증번호와 일치하는지 확인하는 메소드
     *
     * @version 0.1
     * @author 박성수
     * @param email (유저 이메일)
     * @param token (이메일 인증번호)
     * @return true/false (인증번호가 일치하면 true, 일치하지 않으면 false 리턴)
     * @throws InvalidTokenException - 인증번호가 만료된 경우, 예외 발생
     */
    public boolean validateToken(String email, String token) {
        // Key 생성
        String key = createKey(email);

        // 인증번호가 만료된 경우, 예외 발생
        String findToken = redisServiceProvider.get(key).orElseThrow(() -> {
            throw new InvalidTokenException("인증번호가 만료되었습니다.");
        });

        // 사용자가 보내온 토큰 값과 서버에 저장된 토큰 값을 비교
        return findToken.equals(token);
    }

    private String createKey(String email) {
        return AuthType.MAIL + ":" + email;
    }

    private String createValue() {
        String value = "";

        for (int i=0; i<tokenDigit; i++) {
            value += rd.nextInt(10);
        }
        return value;
    }
}
