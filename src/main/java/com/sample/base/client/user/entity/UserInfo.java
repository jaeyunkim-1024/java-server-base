package com.sample.base.client.user.entity;

import com.sample.base.client.user.enums.UserRoles;
import com.sample.base.common.util.CustomTimeUtil;
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
    @Column
    private Long userSeq;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String userName;

    @Column
    private Timestamp createdAt;

    @Column
    private Timestamp updatedAt;

    @Column
    private Timestamp expireAt;

    @Builder.Default
    @Column
    private Boolean isLock = false;

    @Builder.Default
    @Column
    private String userRole = UserRoles.NO_CERT.getType();

    public UserInfo mergeFrom(UserInfo userInfo) {
        String newEmail = Optional.ofNullable(userInfo.getEmail()).orElse(this.email);
        String newUserName = Optional.ofNullable(userInfo.getUserName()).orElse(this.userName);
        String newUserRole = Optional.ofNullable(userInfo.getUserRole()).orElse(this.userRole);
        boolean isUpdate = (!this.email.equals(newEmail) || !this.userName.equals(newUserName) || !this.userRole.equals(newUserRole));
        return UserInfo.builder()
                .userSeq(userInfo.getUserSeq())
                .updatedAt(isUpdate ? CustomTimeUtil.getCurrentTime() : this.updatedAt)
                .email(this.email.equals(newEmail) ? this.email : newEmail)
                .userName(this.userName.equals(newUserName) ? this.userName : newUserName)
                .userRole(this.userRole.equals(newUserRole) ? this.userRole : newUserRole)
                .password(this.password)
                .createdAt(this.createdAt)
                .expireAt(this.expireAt)
                .isLock(this.isLock)
                .build();
    }
}
