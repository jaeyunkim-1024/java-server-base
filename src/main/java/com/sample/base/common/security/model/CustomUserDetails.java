package com.sample.base.common.security.model;

import com.sample.base.client.user.entity.UserInfo;
import com.sample.base.client.user.enums.UserRoles;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Builder
@ToString
public class CustomUserDetails implements UserDetails {
    @Getter
    private Long userSeq;

    private String email;
    private String password;

    @Getter
    private String role;

    @Getter
    private Timestamp createdAt;

    private Boolean isLock;

    @Getter
    private String myName;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+role));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public boolean isEmailCert(){
        return !role.equals(UserRoles.NO_CERT.getType());
    }

    public boolean isAccountLocked(){
        return isLock;
    }

    /// 계정 잠금, IS_LOCKED 로 확인, 보통 비밀번호 연속 틀리면 적용하니
    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    /// 비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    static public CustomUserDetails toPrincipal(@Nullable UserInfo entity){
        if(entity == null){
            return CustomUserDetails.builder().build();
        }
        return CustomUserDetails.builder()
                .userSeq(entity.getUserSeq())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getUserRole())
                .createdAt(entity.getCreatedAt())
                .isLock(entity.getIsLock())
                .myName(entity.getUserName())
                .build();

    }
}
