package com.project.animal.global.common.provider;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsServiceProvider {

    @Value("${spring.sms.from}")
    private String from;

    private final DefaultMessageService smsService;

    public SmsServiceProvider(@Value("${spring.sms.key}") String key,
                              @Value("${spring.sms.secret}") String secret,
                              @Value("${spring.sms.url}") String url) {
        this.smsService = NurigoApp.INSTANCE.initialize(key, secret, url);
    }

    public void sendSms(String phone, String content) {

        // Message 객체 생성
        Message message = new Message();
        
        // 발신, 수신, 문자내용 설정
        message.setFrom(from);
        message.setTo(phone);
        message.setText(content);

        // 메시지 발송
        smsService.sendOne(new SingleMessageSendingRequest(message));
    }
}
