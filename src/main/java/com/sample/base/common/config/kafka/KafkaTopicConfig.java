package com.sample.base.common.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value("${kafka.topic.email.name}")
    private String emailTopicName;

    @Bean
    public NewTopic emailTopic() {
        /// 한번 생성된 토피
        return TopicBuilder
                .name(emailTopicName)
                // 파티션 개수
                .partitions(4)
                // 파티션 복제 계수, 1인 경우 리더 파티션만 존재하는것.
                .replicas(2)
                // kafka borker가 메시지를 어느 시간까지 보관할지 시간이나 byte에 따라서 초과 시, 데이터를 삭제한다.
                // 디스크 크기와 데이터의 중요성에 따라 판단한다.
                .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(1000 * 60 * 60))
                .build();
    }
}
