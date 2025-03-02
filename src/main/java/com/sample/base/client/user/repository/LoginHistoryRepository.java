package com.sample.base.client.user.repository;

import com.sample.base.client.user.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory,Long> {
    int countAllByAccessCd(String accessCd);
}
