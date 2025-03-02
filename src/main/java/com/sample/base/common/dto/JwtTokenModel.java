package com.sample.base.common.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtTokenModel {
    private String token;
    private Long iat;
    private Long exp;
}
