package com.sample.base.user.entity;

import com.sample.base.common.util.CustomTimeUtil;
import com.sample.base.user.enums.UserRoles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Optional;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_INFO")
@Getter
public class UserInfo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "USER_SEQ")
    private Long userSeq;

    @Column(name = "USER_EMAIL")
    private String email;

    @Column(name = "USER_PASSWORD")
    private String password;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "CREATED_AT")
    private Timestamp createdAt;

    @Column(name = "UPDATED_AT")
    private Timestamp updatedAt;

    @Column(name = "EXPIRE_AT")
    private Timestamp expireAt;

    @Builder.Default
    @Column(name = "IS_LOCK")
    private Boolean isLock = false;

    @Builder.Default
    @Column(name = "USER_ROLE")
    private String userRole = UserRoles.NO_CERT.getType();

    public UserInfo mergeFrom(UserInfo userInfo) {
        String newEmail = Optional.ofNullable(userInfo.getEmail()).orElse(this.email);
        String newUserName = Optional.ofNullable(userInfo.getUsername()).orElse(this.username);
        String newUserRole = Optional.ofNullable(userInfo.getUserRole()).orElse(this.userRole);
        boolean isUpdate = (!this.email.equals(newEmail) || !this.username.equals(newUserName) || !this.userRole.equals(newUserRole));
        return UserInfo.builder()
                .userSeq(userInfo.getUserSeq())
                .updatedAt(isUpdate ? CustomTimeUtil.getCurrentTime() : this.updatedAt)
                .email(this.email.equals(newEmail) ? this.email : newEmail)
                .username(this.username.equals(newUserName) ? this.username : newUserName)
                .userRole(this.userRole.equals(newUserRole) ? this.userRole : newUserRole)
                .password(this.password)
                .createdAt(this.createdAt)
                .expireAt(this.expireAt)
                .isLock(this.isLock)
                .build();
    }
}
