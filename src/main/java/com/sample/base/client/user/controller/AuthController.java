package com.sample.base.client.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sample.base.client.user.dto.LogoutDto;
import com.sample.base.client.user.dto.UserInfoDto;
import com.sample.base.client.user.dto.UserInfoJoinRequestDto;
import com.sample.base.client.user.enums.UserRoles;
import com.sample.base.client.user.kafka.model.EmailDto;
import com.sample.base.client.user.kafka.service.EmailProducer;
import com.sample.base.client.user.service.AuthService;
import com.sample.base.client.user.service.CustomUserDetailService;
import com.sample.base.common.dto.CustomResponseDto;
import com.sample.base.common.security.model.CustomUserDetails;
import com.sample.base.common.security.provider.JwtTokenProvider;
import com.sample.base.common.util.VerifyCodeUtil;
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
    private final EmailProducer producer;
    private final CustomUserDetailService customUserDetailService;

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

    @PostMapping(value = "/email/send")
    public CustomResponseDto<Object> sendMessage(HttpServletRequest request) {
        boolean isCert = jwtTokenProvider.isCertUser(request);
        if(isCert) {
            return CustomResponseDto
                    .builder()
                    .data("이미 인증한 회원입니다.")
                    .build();
        }

        String email = jwtTokenProvider.getPrincipal(request);
        String verifyCode = VerifyCodeUtil.generated();
        EmailDto emailDto = EmailDto.builder()
                .email(email)
                .verifyCode(verifyCode)
                .build();
        this.producer.send(emailDto.toString());
        return CustomResponseDto
                .builder()
                .data("이메일이 발송되었습니다.")
                .build();
    }

    @PostMapping(value = "/email/verify")
    public CustomResponseDto<Object> verify(@RequestParam(name = "verifyCode", required = false)String verifyCode, HttpServletRequest request) {
        boolean isCert = jwtTokenProvider.isCertUser(request);
        if(isCert) {
            return CustomResponseDto
                    .builder()
                    .data("이미 인증한 회원입니다.")
                    .build();
        }
        String email = jwtTokenProvider.getPrincipal(request);
        EmailDto dto = EmailDto.builder()
                .email(email)
                .verifyCode(verifyCode)
                .build();
        Object token = "인증코드를 확인해주세요.";
        if(authService.verifyCode(dto) != -1){
            /// 토큰 재발급
            try{
                /// 기존 토큰 만료
                jwtTokenProvider.tokenExpire(email);

                /// 신규 토큰 발급
                CustomUserDetails customUserDetails = customUserDetailService.loadUserByUsername(email);
                token = jwtTokenProvider.reIssuedToken(email,customUserDetails,"ROLE_"+ UserRoles.USER.getType());
            }catch (JsonProcessingException e) {
                token = e.getMessage();
            }
        }
        return CustomResponseDto
                .builder()
                .data(token)
                .build();
    }
}
