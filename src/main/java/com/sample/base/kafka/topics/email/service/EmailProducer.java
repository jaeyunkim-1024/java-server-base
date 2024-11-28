package com.sample.base.kafka.topics.email.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${kafka.topic.email.name}")
    private String TOPIC;

    public void send(String message){
        kafkaTemplate.send(TOPIC,message);
    }
}
