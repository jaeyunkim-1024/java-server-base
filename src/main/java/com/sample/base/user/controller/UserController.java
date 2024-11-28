package com.sample.base.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sample.base.common.model.CustomResponseEntity;
import com.sample.base.common.model.JwtTokenModel;
import com.sample.base.security.model.CustomUserDetails;
import com.sample.base.security.provider.JwtTokenProvider;
import com.sample.base.user.dto.LogoutDto;
import com.sample.base.user.dto.UserInfoDto;
import com.sample.base.user.dto.UserInfoJoinRequestDto;
import com.sample.base.user.dto.UserInfoUpdateRequestDto;
import com.sample.base.user.enums.UserRoles;
import com.sample.base.user.service.CustomUserDetailService;
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
@RequestMapping("/api/users")
public class UserController {
    private final UserInfoService userInfoService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;

    @PostMapping("/signup")
    public ResponseEntity<CustomResponseEntity<UserInfoDto>> singUp(@Valid @RequestBody UserInfoJoinRequestDto dto) {
        UserInfoDto data = userInfoService.signUp(dto);
        return ResponseEntity
                .ok(new CustomResponseEntity<>(data));
    }

    @DeleteMapping("/signout")
    public ResponseEntity<CustomResponseEntity<LogoutDto>> logout(HttpServletRequest request) {
        String email = jwtTokenProvider.getPrincipal(request);
        Boolean data = jwtTokenProvider.tokenExpire(email);
        if(data){
            userInfoService.logout(email);
        }
        CustomResponseEntity<LogoutDto> result = new CustomResponseEntity<>(
                new LogoutDto(data));
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/update/{userSeq}")
    public CustomResponseEntity<Object> update(@RequestBody UserInfoUpdateRequestDto dto, @PathVariable Long userSeq, HttpServletRequest request) throws JsonProcessingException {
        String email = jwtTokenProvider.getPrincipal(request);
        if(!email.equals(dto.getEmail())){
            dto.setUserRole(UserRoles.NO_CERT.getType());
        }
        UserInfoDto data = userInfoService.update(userSeq, dto, email);
        /// 수정  결과, 이메일이 같으면,
        if(email.equals(data.getEmail())){
            return CustomResponseEntity
                    .builder()
                    .data(data)
                    .build();
        }
        /// 기존 토큰 만료
        jwtTokenProvider.tokenExpire(email);

        /// 신규 토큰 재발급
        CustomUserDetails principal = customUserDetailService.loadUserByUsername(data.getEmail());
        JwtTokenModel token = jwtTokenProvider.reIssuedToken(data.getEmail(),principal,"ROLE_"+principal.getRole());
        return CustomResponseEntity
                .builder()
                .data(token)
                .build();
    }
}
