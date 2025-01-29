package com.sample.base.security.handler;

import com.sample.base.user.entity.LoginHistory;
import com.sample.base.user.enums.AccessCode;
import com.sample.base.user.repository.LoginHistoryRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final LoginHistoryRepository loginHistoryRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if(exception instanceof AuthenticationServiceException){
            if(!exception.getMessage().isEmpty()){
                LoginHistory loginHistory = LoginHistory.builder()
//                        .email(exception.getMessage())
                        .accessCd(AccessCode.LOGIN_FAILED_INVALID_PASSWORD.getCode())
                        .build();
                loginHistoryRepository.save(loginHistory);
            }
        }else{
            if(exception instanceof BadCredentialsException){
                LoginHistory loginHistory = LoginHistory.builder()
//                        .email(exception.getMessage())
                        .accessCd(AccessCode.LOGIN_FAILED_NOT_CORRECT_EMAIL_PWD.getCode())
                        .build();
                loginHistoryRepository.save(loginHistory);
            }
        }
        super.onAuthenticationFailure(request, response, exception);
    }
}
