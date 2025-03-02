package com.sample.base.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {
    private final StringRedisTemplate redisTemplate;
    private final TimeUnit unit = TimeUnit.SECONDS;

    public void setValue(String key, String value, long expirationTime){
        setStringValue(key,value,expirationTime);
    }

    public void setEnableToken(String token, String email, long expirationTime) {
        setStringValue(email,token,expirationTime);
        setStringValue(token,"Y",expirationTime);
    }

    public void setDisableToken(String email) {
        String token = getValue(email);
        Long expirationTime = redisTemplate.getExpire(token, unit);
        setStringValue(token,"N", Objects.requireNonNull(expirationTime));
    }

    public boolean isEnableToken(String token) {
        // expire == -2: 키 X
        // expire == -1: 키는 존재하지만 만료되지 않음
        // expire >= 0: 남은 시간
        long expire = Optional.ofNullable(redisTemplate.getExpire(token, unit)).orElse(-2L);
        return (expire == -1L || expire > 0L) && getValue(token).equals("Y");
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
