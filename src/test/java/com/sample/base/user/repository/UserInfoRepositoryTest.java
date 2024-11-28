package com.sample.base.user.repository;

import com.sample.base.user.entity.UserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserInfoRepositoryTest {
    final String email = "rlawodbs1024@gmail.com";
    final String username = "김재윤-JunitTest";
    final String password = "1234";

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Test
    @DisplayName("UserInfo_insert테스트")
    void UserInfo_INSERT_테스트() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        UserInfo entity = UserInfo.builder()
                        .email(email)
                        .username(username)
                        .password(password)
                        .createdAt(timestamp)
                        .updatedAt(timestamp)
                        .expireAt(timestamp)
                        .build();

        userInfoRepository.save(entity);
        UserInfo r = userInfoRepository.findById(entity.getUserSeq()).get();
        assertEquals(email,r.getEmail());
    }

    @Test
    @DisplayName("이메일로 계정 찾기")
    void 이메일_find_테스트(){
        UserInfo r = userInfoRepository.findUserInfoByEmail(email);
        assertEquals(email,r.getEmail());
    }
}