package com.sample.base.client.user.entity;


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
@Table
@Getter
public class LoginHistory {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column
    private Long loginHistorySeq;

    @Column
    private Long userSeq;

    @UpdateTimestamp
    @Column
    private Timestamp loginAccessTime;

    @Column
    private String accessCd;
}
