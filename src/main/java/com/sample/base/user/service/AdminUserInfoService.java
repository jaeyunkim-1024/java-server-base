package com.sample.base.user.service;

import com.sample.base.security.provider.JwtTokenProvider;
import com.sample.base.user.entity.UserInfo;
import com.sample.base.user.repository.LoginHistoryRepository;
import com.sample.base.user.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public Boolean expireToken(Long userId) {
        UserInfo userInfo = userInfoRepository.findById(userId).orElse(null);
        if(userInfo != null) {
            String key = userInfo.getEmail();
            return jwtTokenProvider.tokenExpire(key);
        }
        return false;
    }
}
