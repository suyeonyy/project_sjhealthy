package com.example.sjhealthy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender(){
        // JavaMailSender: 스프링 프레임워크에서 이메일을 보낼 때 사용하는 인터페이스
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost("smtp.naver.com");
        javaMailSender.setUsername("kongjy621@naver.com");
        javaMailSender.setPassword("Aatjsgml1!"); // 일단 공란으로
        javaMailSender.setPort(465);
        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    public Properties getMailProperties(){
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.debug", "true");
        properties.setProperty("mail.smtp.ssl.trust","smtp.naver.com");
        properties.setProperty("mail.smtp.ssl.enable","true");

        return properties;
    }
}
