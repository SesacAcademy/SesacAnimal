package com.project.animal.global.common.provider;

import com.project.animal.global.common.constant.TokenType;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailTokenProvider {

    @Value("${spring.mail.token.digit}")
    private int tokenDigit;

    @Value("${spring.mail.token.validTime}")
    private int tokenValidTime;                     //  Second 단위

    private final Random rd = new Random();

    private final RedisTemplate<String, String> template;

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
        saveToken(key, value);

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
        String findToken = getToken(key).orElseThrow(() -> {
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

    private Optional<String> getToken(String key) {
        ValueOperations<String, String> operation = template.opsForValue();
        String token = operation.get(key);

        return Optional.ofNullable(token);
    }

    private void saveToken(String key, String value) {
        ValueOperations<String, String> operation = template.opsForValue();
        operation.set(key, value);
    }

    private void sendMail(String email, String token) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject("[새싹 애니멀] 이메일 인증 번호입니다.");
        mail.setText("인증번호 : " + token);
        mailSender.send(mail);
    }
}
