package com.sample.base.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {
    private final StringRedisTemplate redisTemplate;
    private final TimeUnit unit = TimeUnit.SECONDS;

    // expire == -2: 키 X
    // expire == -1: 키는 존재하지만 만료되지 않음 >> 만료시간 무한
    // expire >= 0: 남은 시간

    public void setValue(String key, String value, long expirationTime){
        setStringValue(key,value,expirationTime);
    }

    public boolean isExist(String key){
        long expire = Optional.ofNullable(redisTemplate.getExpire(key, unit)).orElse(-2L);
        return expire == -1L || expire > 0L;
    }

    public boolean deleteKey(String key){
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public String getValue(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key)).orElse("");
    }

    private void setStringValue(String key, String value, Long expirationTime) {
        redisTemplate.opsForValue().set(key, value, Integer.parseInt(expirationTime.toString()), unit);
    }
}
