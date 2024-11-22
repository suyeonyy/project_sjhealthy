package com.example.sjhealthy.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
public class MailServiceImpl implements MailService{
    @Autowired
    private JavaMailSender emailSender; // MailConfig에서 Bean으로 등록

    private String ePw; // 인증번호

    @Override
    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        // 인증 메일 구성
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setFrom(new InternetAddress("kongjy621@naver.com", "sjhealthy"));
        message.setSubject("[sjhealthy]비밀번호를 찾기 위한 인증 메일입니다.");

        String msg = "<h1>안녕하세요.</h1>";
        msg += "<p>아래의 인증번호를 창에 입력해주세요.</p>";
        msg += "<br><br>";
        msg += "<strong>" + ePw + "</strong>";

        message.setText(msg, "UTF-8", "html");

        return message;
    }

    @Override
    public String createKey() {
        // 랜덤 클래스를 이용한 인증코드 생성
        int leftLimit = 48; // 0
        int rigthLimit = 122; // z => 0~z까지
        int stringLength = 10; // 인증번호 길이

        Random random = new Random();
        String key = random.ints(leftLimit, rigthLimit + 1) // 범위 제한, intstream 을 반환
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)) // 0~9 / a~z / A~Z 조합으로 생성
            .limit(stringLength) // 스트림 크기
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();

        return key;
    }

    @Override
    public String sendMessage(String to) throws Exception {
        ePw = createKey(); // 인증번호 생성
        MimeMessage message = createMessage(to); // 메시지 작성(필드에 ePw 선언해놔서 그 값을 담아 보냄)

        try {
            emailSender.send(message);
        } catch (Exception e){
            throw new IllegalArgumentException();
        }
        return ePw; // 서버에서 받아 입력한 것과 비교하여 인증번호 확인에 사용
    }
}
