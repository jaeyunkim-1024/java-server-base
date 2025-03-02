package com.sample.base.common.service;

import com.sample.base.common.config.smtp.SmTpConfig;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmtpMailService implements EmailService<Boolean> {
    private final SmTpConfig mailProperties;
    private final JavaMailSender mailSender;

    @Override
    public Boolean sendEmail(String to, String title, String content) {
        if (!StringUtils.hasLength(to))
            return false;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, "UTF-8");

            messageHelper.setSubject(title);
            messageHelper.setText(content, true);
            messageHelper.setFrom(mailProperties.getFromMail());

            String[] toArr = to.split(",");

            messageHelper.setTo(toArr);

            mailSender.send(message); // 메일발송
            return true;
        } catch(Exception e){
            log.error(e.getMessage(), e);
            return false;
        }
    }
}
