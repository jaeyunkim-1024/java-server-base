package com.sample.base.user.repository;

import com.sample.base.user.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory,Long> {
    int countAllByAccessCd(String accessCd);
}
