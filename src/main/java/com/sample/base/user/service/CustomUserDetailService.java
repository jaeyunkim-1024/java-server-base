package com.sample.base.user.service;

import com.sample.base.security.model.CustomUserDetails;
import com.sample.base.user.repository.UserInfoRepository;
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
