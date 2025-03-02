package com.sample.base.client.user.service;

import com.sample.base.client.user.repository.UserInfoRepository;
import com.sample.base.common.security.model.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailService implements UserDetailsService {
    private final UserInfoRepository userInfoRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) {
        return CustomUserDetails.toPrincipal(
                Optional.ofNullable(userInfoRepository.findUserInfoByEmail(email)).orElseThrow(
                        () -> new UsernameNotFoundException(email)
                )
        );
    }
}
