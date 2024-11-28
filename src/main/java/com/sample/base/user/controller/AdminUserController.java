package com.sample.base.user.controller;

import com.sample.base.common.model.CustomResponseEntity;
import com.sample.base.user.dto.LoginHistoryDto;
import com.sample.base.user.dto.LogoutDto;
import com.sample.base.user.service.AdminUserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {
    private final AdminUserInfoService adminUserInfoService;

    @GetMapping("/{userId}/logs")
    public ResponseEntity<Page<LoginHistoryDto>> logs(@PathVariable Long userId, @RequestParam(required = false, name = "pageNum", defaultValue = "0") Integer pageNum) {
        Pageable pageable = PageRequest.of(pageNum, 50);
        Page<LoginHistoryDto> list = adminUserInfoService.list(userId,pageable);
        return ResponseEntity
                .ok()
                .body(list);
    }

    @PostMapping("/{userId}/expire-tokens")
    public ResponseEntity<CustomResponseEntity<LogoutDto>> expireTokens(@PathVariable Long userId) {
        Boolean success = adminUserInfoService.expireToken(userId);
        CustomResponseEntity<LogoutDto> result = new CustomResponseEntity<>(
                new LogoutDto(success));
        return ResponseEntity.ok(result);
    }
}
