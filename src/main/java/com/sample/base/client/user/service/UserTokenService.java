package com.sample.base.client.user.service;

import com.sample.base.common.enums.RedisPrefixEnum;
import com.sample.base.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserTokenService {
    private final RedisService redisService;

    public void setEnableToken(String token, String email, long expirationTime) {
        String emailTokenKey = RedisPrefixEnum.USER_TOKEN.prefix + ":" + email;
        /// 유저 토큰 맵핑
        redisService.setValue(emailTokenKey,token,expirationTime);
        /// 토큰 관리
        redisService.setValue(token,"Y",expirationTime);
    }

    public void setDisableToken(String email) {
        String emailTokenKey = RedisPrefixEnum.USER_TOKEN.prefix + ":" + email;
        String token = redisService.getValue(emailTokenKey);
        if(redisService.isExist(token) && redisService.getValue(token).equals("Y")){
            redisService.setValue(token,"N", 3600);
        }
    }

    public boolean isEnableToken(String token) {
        if(!redisService.isExist(token)){
            return true;
        }
        String isYn = redisService.getValue(token);
        return isYn.equals("Y");
    }
}
