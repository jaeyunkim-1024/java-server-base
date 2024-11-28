package com.sample.base.user.service;

import com.sample.base.security.provider.JwtTokenProvider;
import com.sample.base.user.dto.LoginHistoryDto;
import com.sample.base.user.entity.LoginHistory;
import com.sample.base.user.entity.UserInfo;
import com.sample.base.user.enums.AccessCode;
import com.sample.base.user.repository.LoginHistoryRepository;
import com.sample.base.user.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public Page<LoginHistoryDto> list(Long userId, Pageable pageable) {
        UserInfo userInfo = userInfoRepository.findById(userId).orElse(null);
        if(userInfo == null) {
            return Page.empty();
        }
        List<LoginHistory> list = loginHistoryRepository.findAllByEmail(userInfo.getEmail(), pageable);
        Map<String, AccessCode> map = AccessCode.getAccessCodeMap();
        Page<LoginHistoryDto> result = new PageImpl<>(list.stream().map((eneity) -> LoginHistoryDto.fromEntity(eneity,map)).toList(), pageable, list.size());
        return result;
    }

    public Boolean expireToken(Long userId) {
        UserInfo userInfo = userInfoRepository.findById(userId).orElse(null);
        if(userInfo != null) {
            String key = userInfo.getEmail();
            return jwtTokenProvider.tokenExpire(key);
        }
        return false;
    }
}
