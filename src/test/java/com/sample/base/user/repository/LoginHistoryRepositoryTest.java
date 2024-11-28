package com.sample.base.user.repository;

import com.sample.base.user.entity.LoginHistory;
import com.sample.base.user.enums.AccessCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class LoginHistoryRepositoryTest {
    @Autowired
    private LoginHistoryRepository loginHistoryRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    private Long userSeq = 11L;

    @Test
    @DisplayName("로그인 이력 insert 테스트")
    void 로그인_이력_테스트(){
        LoginHistory loginHistory = LoginHistory.builder()
                .accessCd(AccessCode.LOGIN_SUCCESS.getCode())
                .build();

        loginHistoryRepository.save(loginHistory);
        assertNotNull(loginHistory.getLoginHistorySeq());
        assertNotNull(loginHistory.getLoginAccessTime());
    }
}