package com.sample.base.client.user.dto;

import com.sample.base.client.user.entity.UserInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoUpdateRequestDto {
    private String username;

    private String email;

    private String userRole;

    public UserInfo toEntity(Long userSeq){
        return UserInfo.builder()
                .userSeq(userSeq)
                .userName(username)
                .email(email)
                .userRole(userRole)
                .build();
    }

}
