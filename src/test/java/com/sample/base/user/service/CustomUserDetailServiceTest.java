package com.sample.base.user.service;

import com.sample.base.security.model.CustomUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CustomUserDetailServiceTest {
    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Test
    @DisplayName("로그인 서비스 테스트")
    void 로그인(){
        String email = "rlawodbs1024@gmail.com";
        CustomUserDetails r = customUserDetailService.loadUserByUsername(email);
        assertEquals(email,r.getUsername());
    }

}