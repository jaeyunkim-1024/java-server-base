package com.sample.base.security.handler;

import com.sample.base.client.user.entity.LoginHistory;
import com.sample.base.client.user.entity.UserInfo;
import com.sample.base.client.user.enums.AccessCode;
import com.sample.base.client.user.repository.LoginHistoryRepository;
import com.sample.base.client.user.repository.UserInfoRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final UserInfoRepository userInfoRepository;
    private final LoginHistoryRepository loginHistoryRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if(exception instanceof BadCredentialsException){
            if(!exception.getMessage().equals("Bad credentials")){
                UserInfo user = userInfoRepository.findUserInfoByEmail(exception.getMessage());
                if(user != null){
                    LoginHistory loginHistory = LoginHistory.builder()
                            .userSeq(user.getUserSeq())
                            .accessCd(AccessCode.LOGIN_FAILED_NOT_CORRECT_EMAIL_PWD.getCode())
                            .build();
                    loginHistoryRepository.save(loginHistory);
                }
            }
        }
        super.onAuthenticationFailure(request, response, exception);
    }

}
