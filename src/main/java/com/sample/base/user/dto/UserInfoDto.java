package com.sample.base.user.dto;

import com.sample.base.user.entity.UserInfo;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@Builder
@ToString
public class UserInfoDto {
    private String email;
    private String username;
    private Long createdAt;

    public static UserInfoDto fromEntity(UserInfo entity) {
        return UserInfoDto.builder()
                .email(entity.getEmail())
                .username(entity.getUsername())
                .createdAt(entity.getCreatedAt().getTime())
                .build();
    }

    public static <K,V> UserInfoDto Map2Dto(Map<K,V> map) {
        return UserInfoDto.builder()
                .email(map.get("email").toString())
                .username(map.get("username").toString())
                .createdAt(Long.parseLong(map.get("createdAt").toString()))
                .build();
    }
}
