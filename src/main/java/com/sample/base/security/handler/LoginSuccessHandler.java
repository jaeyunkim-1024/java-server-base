package com.sample.base.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.base.common.model.JwtTokenModel;
import com.sample.base.security.model.CustomUserDetails;
import com.sample.base.security.provider.JwtTokenProvider;
import com.sample.base.user.entity.LoginHistory;
import com.sample.base.user.entity.UserInfo;
import com.sample.base.user.enums.AccessCode;
import com.sample.base.user.repository.LoginHistoryRepository;
import com.sample.base.user.repository.UserInfoRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final LoginHistoryRepository loginHistoryRepository;
    private final UserInfoRepository userInfoRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        JwtTokenModel token = jwtTokenProvider.issuedToken(authentication);
        insertHistory(authentication);
        // JSON 응답 출력
        response.addHeader("Content-Type", "application/json; charset=UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(mapper.writeValueAsString(token));
        response.getWriter().flush();
    }

    private void insertHistory(Authentication authentication) {
        CustomUserDetails d = (CustomUserDetails) authentication.getPrincipal();
        UserInfo userInfo = userInfoRepository.findUserInfoByEmail(d.getUsername());
        LoginHistory loginHistory = LoginHistory.builder()
                .accessCd(AccessCode.LOGIN_SUCCESS.getCode())
                .userSeq(userInfo.getUserSeq())
//                .email(d.getUsername())
                .build();
        loginHistoryRepository.save(loginHistory);
    }
}
