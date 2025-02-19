package com.example.sjhealthy.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public interface MailService {
    // 메일 내용 작성
    MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException;

    // 메일에서 사용할 랜덤 인증코드 생성
    String createKey();

    // 메일 발송
    String sendMessage(String to) throws Exception;


}
