package com.sample.base.user.controller;

import com.sample.base.common.dto.CustomResponseDto;
import com.sample.base.security.provider.JwtTokenProvider;
import com.sample.base.user.dto.LogoutDto;
import com.sample.base.user.dto.UserInfoDto;
import com.sample.base.user.dto.UserInfoJoinRequestDto;
import com.sample.base.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/sign-up")
    public ResponseEntity<CustomResponseDto<UserInfoDto>> singUp(@Valid @RequestBody UserInfoJoinRequestDto dto) {
        return ResponseEntity
                .ok(
                    CustomResponseDto.<UserInfoDto>builder()
                            .data(authService.signUp(dto))
                            .build()
                );
    }

    @DeleteMapping("/sign-out")
    public ResponseEntity<CustomResponseDto<LogoutDto>> logout(HttpServletRequest request) {
        String email = jwtTokenProvider.getPrincipal(request);
        Boolean data = jwtTokenProvider.tokenExpire(email);
        if(data){
            authService.logout(email);
        }
        CustomResponseDto<LogoutDto> result = CustomResponseDto.<LogoutDto>builder()
                .data(new LogoutDto(data))
                .build();
        return ResponseEntity
                .ok(
                    CustomResponseDto.<LogoutDto>builder()
                            .data(new LogoutDto(data))
                            .build()
                );
    }
}
