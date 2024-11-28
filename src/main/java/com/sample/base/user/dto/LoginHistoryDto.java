package com.sample.base.user.dto;

import com.sample.base.user.entity.LoginHistory;
import com.sample.base.user.enums.AccessCode;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class LoginHistoryDto {
    private Long loginHistorySeq;
    private String email;
    private String desc;

    public static LoginHistoryDto fromEntity(LoginHistory loginHistory, Map<String,AccessCode> map) {
        return LoginHistoryDto
                .builder()
                .loginHistorySeq(loginHistory.getLoginHistorySeq())
                .email(loginHistory.getEmail())
                .desc(map.get(loginHistory.getAccessCd()).getDesc())
                .build();
    }
}
