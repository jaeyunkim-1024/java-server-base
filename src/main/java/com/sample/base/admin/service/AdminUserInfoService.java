package com.sample.base.admin.service;

import com.sample.base.client.user.entity.UserInfo;
import com.sample.base.client.user.enums.UserRoles;
import com.sample.base.client.user.repository.UserInfoRepository;
import com.sample.base.common.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public Boolean expireToken(Long userId) {
        UserInfo userInfo = userInfoRepository.findById(userId).orElse(null);
        if(userInfo != null && userInfo.getUserRole().equals(UserRoles.USER.getType())) {
            String key = userInfo.getEmail();
            return jwtTokenProvider.tokenExpire(key);
        }
        return false;
    }
}
