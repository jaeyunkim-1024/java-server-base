package com.sample.base.kafka.topics.email.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sample.base.common.model.CustomResponseEntity;
import com.sample.base.common.util.VerifyCodeUtil;
import com.sample.base.kafka.topics.email.model.EmailDto;
import com.sample.base.kafka.topics.email.service.EmailProducer;
import com.sample.base.security.model.CustomUserDetails;
import com.sample.base.security.provider.JwtTokenProvider;
import com.sample.base.user.enums.UserRoles;
import com.sample.base.user.service.CustomUserDetailService;
import com.sample.base.user.service.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/kafka/topic/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailProducer producer;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserInfoService userInfoService;
    private final CustomUserDetailService customUserDetailService;

    @PostMapping(value = "/send")
    public CustomResponseEntity<Object> sendMessage(HttpServletRequest request) {
        boolean isCert = jwtTokenProvider.isCertUser(request);
        if(isCert) {
            return CustomResponseEntity
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
        return CustomResponseEntity
                .builder()
                .data("이메일이 발송되었습니다.")
                .build();
    }

    @PostMapping(value = "/verify")
    public CustomResponseEntity<Object> verify(@RequestParam(name = "verifyCode", required = false)String verifyCode, HttpServletRequest request) {
        boolean isCert = jwtTokenProvider.isCertUser(request);
        if(isCert) {
            return CustomResponseEntity
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
        if(userInfoService.verifyCode(dto) != -1){
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
        return CustomResponseEntity
                .builder()
                .data(token)
                .build();
    }
}
