package com.sample.base.user.repository;

import com.sample.base.user.entity.LoginHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory,Long> {
    List<LoginHistory> findAllByEmail(String email, Pageable pageable);
}
