package com.project.animal.global.common.provider;

import com.project.animal.global.common.constant.TokenType;
import com.project.animal.member.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
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

    private final MailSender mailSender;

    /**
     * 이메일 인증번호 생성하는 메소드
     * @param email     (유저 이메일)
     * @return String   (인증 번호)
     * @Author sHu
     * @Date 23.11.03
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

        // 유저에게 토큰 발송 (메일)
        sendMail(email, value);
    }

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
        return TokenType.MAIL + ":" + email;
    }

    private String createValue() {
        String value = "";

        for (int i=0; i<tokenDigit; i++) {
            value += rd.nextInt(10);
        }
        return value;
    }

    private void sendMail(String email, String token) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject("[새싹 애니멀] 이메일 인증 번호입니다.");
        mail.setText("인증번호 : " + token);
        mailSender.send(mail);
    }
}
