package com.sample.base.common.config.smtp;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MailConfig {
    private final SmTpConfig mailProperties;
    Properties pt = new Properties();

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(this.mailProperties.getHost());
        javaMailSender.setUsername(this.mailProperties.getUsername());
        javaMailSender.setPassword(this.mailProperties.getPassword());
        javaMailSender.setPort(this.mailProperties.getPort());

        pt.put("mail.smtp.socketFactory.port", this.mailProperties.getPort());
        pt.put("mail.smtp.auth", true);
        pt.put("mail.smtp.starttls.enable", true);
        pt.put("mail.smtp.starttls.required", true);
        pt.put("mail.smtp.socketFactory.fallback", false);
        pt.put("mail.smtp.socketFactory.class", this.mailProperties.getSocketFactoryClass());

        javaMailSender.setJavaMailProperties(pt);
        javaMailSender.setDefaultEncoding("UTF-8");

        return javaMailSender;
    }
}
