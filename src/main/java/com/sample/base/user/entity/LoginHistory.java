package com.sample.base.user.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LOGIN_HISTORY")
@Getter
public class LoginHistory {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "LOGIN_HISTORY_SEQ")
    private Long loginHistorySeq;

    @Column(name = "USER_SEQ")
    private Long userSeq;

    @UpdateTimestamp
    @Column(name = "LOGIN_ACCESS_TIME")
    private Timestamp loginAccessTime;

    @Column(name = "ACCESS_CD")
    private String accessCd;
}
