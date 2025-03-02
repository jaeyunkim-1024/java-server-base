package com.sample.base.client.user.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.base.client.user.dto.EmailAuthDto;
import com.sample.base.common.config.smtp.SmTpConfig;
import com.sample.base.common.service.RedisService;
import com.sample.base.common.service.SmtpMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailConsumer {
    private final RedisService redisService;
    private final SmtpMailService smtpMailService;
    private final SmTpConfig config;

    @KafkaListener(topics = "${kafka.topic.email.name}", groupId = "${kafka.topic.email.group-id}")
    public void listener(ConsumerRecord<String, String> data) throws JsonProcessingException {
        EmailAuthDto emailAuthDto = new ObjectMapper().readValue(data.value(), EmailAuthDto.class);
        redisService.setValue(emailAuthDto.generateRedisKey(),emailAuthDto.getVerifyCode(), 5 * 60L);

        /// 이메일 전송
        String to = config.getFromMail();
        smtpMailService.sendEmail(to,"이메일 인증 코드", emailAuthDto.getVerifyCode());
    }
}
