package com.sample.base.user.service;

import com.sample.base.user.dto.UserInfoDto;
import com.sample.base.user.dto.UserInfoJoinRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class UserInfoServiceTest {
    static UserInfoJoinRequestDto userInfoJoinRequestDto;
    static String email = "rlawodbs1024@gmail.com";
    static String username = "김재윤";
    @Autowired
    private UserInfoService userInfoService;

    @BeforeAll
    static void setUp(){
        userInfoJoinRequestDto = UserInfoJoinRequestDto.builder()
                .email(email)
                .username(username)
                .build();
    }

    @Test
    @DisplayName("회원 가입 테스트 최소 숫자")
    void 회원가입_테스트_유효성_최소() {
        String minPwd = "12345";
        userInfoJoinRequestDto.setPassword(minPwd);
        UserInfoDto r = null;
        try{
            r = userInfoService.signUp(userInfoJoinRequestDto);
        }catch (IllegalArgumentException iae){
            System.out.println(iae.getMessage());
        }
        assertNull(r);
    }

    @Test
    @DisplayName("회원 가입 테스트 최대 숫자")
    void 회원가입_테스트_유효성_최대() {
        String maxPwd = "12345";
        userInfoJoinRequestDto.setPassword(maxPwd);
        UserInfoDto r = null;
        try{
            r = userInfoService.signUp(userInfoJoinRequestDto);
        }catch (IllegalArgumentException iae){
            System.out.println(iae.getMessage());
        }
        assertNull(r);
    }

    @Test
    @DisplayName("회원 가입 서비스 테스트")
    void 회원가입_테스트() {
        String pwd = "fdjsalkfjsda";
        userInfoJoinRequestDto.setPassword(pwd);
        UserInfoDto r = userInfoService.signUp(userInfoJoinRequestDto);
        assertEquals(email,r.getEmail());
    }
}