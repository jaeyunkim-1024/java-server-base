package com.sample.base.client.user.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.base.client.user.dto.EmailAuthDto;
import com.sample.base.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailProducer {
    private final RedisService redisService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private ObjectMapper mapper = new ObjectMapper();

    @Value("${kafka.topic.email.name}")
    private String TOPIC;

    public void sendTo(String to) {
        String verifyCode = getVerifyCode();
        try{
            EmailAuthDto dto = EmailAuthDto.builder()
                    .email(to)
                    .verifyCode(verifyCode)
                    .build();
            String message = mapper.writeValueAsString(dto);
            kafkaTemplate.send(TOPIC,message);
        }catch(JsonProcessingException e){
            log.error("JsonProcessingException", e);
            redisService.deleteKey(verifyCode);
        }
    }

    private String getVerifyCode(){
        String verifyCode = generatedVerifyCode();
        while(redisService.isExist(verifyCode)){
            verifyCode = generatedVerifyCode();
        }
        // 한번 채번한 코드는 3시간 동안 사용 불가
        redisService.setValue(verifyCode, "N", 3 * 60 * 60);
        return verifyCode;
    }

    private String generatedVerifyCode(){
        // SecureRandom 객체 생성
        SecureRandom secureRandom = new SecureRandom();

        // 6자리 랜덤 숫자 생성
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            // 1~9 사이의 랜덤 숫자 생성
            int digit = secureRandom.nextInt(9) + 1; // 0~8 -> 1~9
            randomNumber.append(digit);
        }
        return randomNumber.toString();
    }
}
