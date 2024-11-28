package com.sample.base.user.repository;

import com.sample.base.user.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
    int countUserInfoByEmail(String email);

    UserInfo findUserInfoByEmail(String email);
}
