package com.sample.base.admin.controller;

import com.sample.base.admin.service.AdminUserInfoService;
import com.sample.base.client.user.dto.LogoutDto;
import com.sample.base.common.dto.CustomResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private final AdminUserInfoService adminUserInfoService;

    @PostMapping("/{userId}/expire-tokens")
    public ResponseEntity<CustomResponseDto<LogoutDto>> expireTokens(@PathVariable Long userId) {
        Boolean success = adminUserInfoService.expireToken(userId);
        return ResponseEntity.ok(
                CustomResponseDto.<LogoutDto>builder()
                        .data(new LogoutDto(success))
                        .build()
        );
    }
}
