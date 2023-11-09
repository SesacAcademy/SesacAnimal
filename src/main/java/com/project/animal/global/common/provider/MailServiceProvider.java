package com.project.animal.global.common.provider;

import com.project.animal.global.common.dto.MailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailServiceProvider {

    private final MailSender mailSender;

    /**
     * 단일 사용자에게 메일 발송하는 메소드
     *
     * @version 0.1
     * @author 박성수
     * @param email (메일 수신자)
     * @param mails (메일)
     * @throws MailSendException - 메일 발송에 실패할 시, 예외 발생
     */
    public void sendMail(String email, MailDto... mails) {
        List<SimpleMailMessage> mailList = new ArrayList<>();
        
        // MailDto --> SimpleMailMessage 변환
        Arrays.stream(mails).forEach(mail -> {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);                        // 수신자 설정
            msg.setSubject(mail.getTitle());         // 제목 설정
            msg.setText(mail.getContent());          // 내용 설정
            mailList.add(msg);
        });

        // 메일 발송
        mailSender.send(mailList.toArray(SimpleMailMessage[]::new));
    }

    /**
     * 여러 명의 사용자에게 메일 발송하는 메소드
     *
     * @version 0.1
     * @author 박성수
     * @param emailList (메일 수신자 목록)
     * @param mails (메일)
     * @throws MailSendException - 메일 발송에 실패할 시, 예외 발생
     */
    public void sendMail(List<String> emailList, MailDto... mails) {
        List<SimpleMailMessage> mailList = new ArrayList<>();

        // MailDto --> SimpleMailMessage 변환
        Arrays.stream(mails).forEach(mail -> {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(emailList.toArray(String[]::new));        // 수신자 설정
            msg.setSubject(mail.getTitle());                    // 제목 설정
            msg.setText(mail.getContent());                     // 내용 설정
            mailList.add(msg);
        });

        // 메일 발송
        mailSender.send(mailList.toArray(SimpleMailMessage[]::new));
    }
}
