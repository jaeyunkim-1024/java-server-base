package com.sample.base.redis.config;

import com.sample.base.common.config.DotEnvScheme;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final Dotenv dotenv;

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setEnableTransactionSupport(false);
        return redisTemplate;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        String host = dotenv.get(DotEnvScheme.REDIS_HOST.name());
        int port = Integer.parseInt(dotenv.get(DotEnvScheme.REDIS_PORT.name()));
        return new LettuceConnectionFactory(host, port);
    }

}
