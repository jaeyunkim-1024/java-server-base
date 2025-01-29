package com.sample.base.kafka.topics.email.service;

import com.sample.base.common.util.VerifyCodeUtil;
import com.sample.base.kafka.topics.email.model.EmailDto;
import com.sample.base.redis.service.RedisService;
import com.sample.base.smtp.config.SmTpConfig;
import com.sample.base.smtp.service.SmtpMailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        EmailDto payload = new ObjectMapper().readValue(data.value(), EmailDto.class);
        redisService.setValue(payload.generateRedisKey(),payload.getVerifyCode(), VerifyCodeUtil.getVerifyExpire());

        /// 이메일 전송
        smtpMailService.sendEmail(config.getFromMail(),"로컬인증이메일", payload.getVerifyCode());
    }
}
