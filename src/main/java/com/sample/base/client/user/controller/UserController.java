package com.sample.base.client.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sample.base.client.user.dto.UserInfoDto;
import com.sample.base.client.user.dto.UserInfoUpdateRequestDto;
import com.sample.base.client.user.enums.UserRoles;
import com.sample.base.client.user.service.UserInfoService;
import com.sample.base.client.user.service.security.CustomUserDetailService;
import com.sample.base.common.dto.CustomResponseDto;
import com.sample.base.common.dto.JwtTokenModel;
import com.sample.base.common.security.model.CustomUserDetails;
import com.sample.base.common.security.provider.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserInfoService userInfoService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;

    @PatchMapping("/update/{userSeq}")
    public CustomResponseDto<Object> update(@RequestBody UserInfoUpdateRequestDto dto, @PathVariable Long userSeq, HttpServletRequest request) throws JsonProcessingException {
        String email = jwtTokenProvider.getPrincipal(request);
        if(!email.equals(dto.getEmail())){
            dto.setUserRole(UserRoles.NO_CERT.getType());
        }
        UserInfoDto data = userInfoService.update(userSeq, dto, email);

        boolean isExpireAndReIssue = !email.equals(data.getEmail());
        if(!isExpireAndReIssue){
            return CustomResponseDto
                    .builder()
                    .data(data)
                    .build();
        }

        /// 기존 토큰 만료
        jwtTokenProvider.tokenExpire(email);

        /// 신규 토큰 재발급
        CustomUserDetails principal = customUserDetailService.loadUserByUsername(data.getEmail());
        JwtTokenModel token = jwtTokenProvider.reIssuedToken(data.getEmail(),principal,"ROLE_"+principal.getRole());

        return CustomResponseDto
                .builder()
                .data(token)
                .build();
    }
}
