package com.sample.base.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.base.user.dto.LoginRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class LoginFilter extends AbstractAuthenticationProcessingFilter {
    public LoginFilter(String loginUrl) {
        super(new AntPathRequestMatcher(loginUrl, HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginRequestDto dto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
        String email = Optional.ofNullable(dto.getEmail()).orElseGet(() -> "");
        String password = Optional.ofNullable(dto.getPassword()).orElseGet(() -> "");
        if(!StringUtils.hasLength(email)) {
            throw new AuthenticationServiceException(email);
        }
        if(!StringUtils.hasLength(password) || password.length() < 6 || password.length() > 20) {
            throw new AuthenticationServiceException(email);
        }
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
