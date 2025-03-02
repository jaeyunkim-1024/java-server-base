package com.sample.base.client.user.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.base.common.enums.RedisPrefixEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuthDto {
    private String email;
    private String verifyCode;

    public String toString() {
        String jsonStr = "";
        try {
            jsonStr = new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            jsonStr = e.getMessage();
        }
        return jsonStr;
    }

    public String generateRedisKey() {
        return RedisPrefixEnum.EMAIL_AUTH.prefix + ":" + email;
    }
}
