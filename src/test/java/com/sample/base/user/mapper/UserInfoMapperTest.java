package com.sample.base.user.mapper;

import com.sample.base.client.user.entity.UserInfo;
import com.sample.base.mappers.UserInfoMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
public class UserInfoMapperTest {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Test
    public void 맵퍼테스트(){
        List<UserInfo> list =  userInfoMapper.selectUserInfo();
        assertFalse(list.isEmpty());
    }
}
