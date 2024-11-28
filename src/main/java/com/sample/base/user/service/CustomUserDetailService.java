package com.sample.base.user.service;

import com.sample.base.security.model.CustomUserDetails;
import com.sample.base.user.entity.UserInfo;
import com.sample.base.user.repository.UserInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailService implements UserDetailsService {
    private final UserInfoRepository userInfoRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) {
        UserInfo entity = userInfoRepository.findUserInfoByEmail(email);
        if(entity == null) {
            throw new UsernameNotFoundException(email);
        }
        return CustomUserDetails.builder()
                .userSeq(entity.getUserSeq())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getUserRole())
                .createdAt(entity.getCreatedAt())
                .isLock(entity.getIsLock())
                .myName(entity.getUsername())
                .build();
    }
}
