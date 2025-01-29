package com.sample.base.user.controller;

import com.sample.base.common.model.CustomResponseEntity;
import com.sample.base.security.provider.JwtTokenProvider;
import com.sample.base.user.dto.LogoutDto;
import com.sample.base.user.dto.UserInfoDto;
import com.sample.base.user.dto.UserInfoJoinRequestDto;
import com.sample.base.user.service.AuthService;
import com.sample.base.user.service.UserInfoService;
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
    public ResponseEntity<CustomResponseEntity<UserInfoDto>> singUp(@Valid @RequestBody UserInfoJoinRequestDto dto) {
        UserInfoDto data = authService.signUp(dto);
        return ResponseEntity
                .ok(new CustomResponseEntity<>(data));
    }

    @DeleteMapping("/sign-out")
    public ResponseEntity<CustomResponseEntity<LogoutDto>> logout(HttpServletRequest request) {
        String email = jwtTokenProvider.getPrincipal(request);
        Boolean data = jwtTokenProvider.tokenExpire(email);
        if(data){
            authService.logout(email);
        }
        CustomResponseEntity<LogoutDto> result = new CustomResponseEntity<>(
                new LogoutDto(data));
        return ResponseEntity.ok(result);
    }
}
