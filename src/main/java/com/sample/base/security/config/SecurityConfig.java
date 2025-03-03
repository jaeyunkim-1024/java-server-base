package com.sample.base.security.config;

import com.sample.base.client.user.enums.UserRoles;
import com.sample.base.client.user.service.security.CustomUserDetailService;
import com.sample.base.security.filter.JwtExceptionFilter;
import com.sample.base.security.filter.JwtFilter;
import com.sample.base.security.filter.LoginFilter;
import com.sample.base.security.handler.LoginFailureHandler;
import com.sample.base.security.handler.LoginSuccessHandler;
import com.sample.base.security.provider.CustomAuthenticationProvider;
import com.sample.base.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.RequestContextFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailService customUserDetailService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, RequestContextFilter requestContextFilter) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((req) ->
                                req
                                        .requestMatchers(
                                                "swagger-ui/index.html"
                                                ,"/error"
                                                ,"/anonymous/health-check"
                                                ,"/api/auth/sign-up"
                                                ,"/api/auth/sign-in"
                                        ).permitAll()
                                        .requestMatchers("/api/admin/**")
                                            .hasRole(UserRoles.ADMIN.getType())
                                        .requestMatchers("/api/auth/sign-out","/api/auth/email/**")
                                            .hasAnyRole(UserRoles.ADMIN.getType(),UserRoles.USER.getType(),UserRoles.NO_CERT.getType())
                                        .anyRequest()
                                            .hasAnyRole(UserRoles.ADMIN.getType(),UserRoles.USER.getType())
                )
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter(), JwtFilter.class)
                .addFilterAt(loginFilter(),UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build(); // build()는 단 한번만 호출
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        CustomAuthenticationProvider customAuthenticationProvider = new CustomAuthenticationProvider();
        customAuthenticationProvider.setUserDetailsService(customUserDetailService);
        customAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return customAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        DaoAuthenticationProvider provider = daoAuthenticationProvider();
        return new ProviderManager(provider);
    }

    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter("/api/auth/sign-in");
        loginFilter.setAuthenticationManager(authenticationManager());
        loginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        loginFilter.setAuthenticationFailureHandler(loginFailureHandler);
        return loginFilter;
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtTokenProvider);
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter(){
        return new JwtExceptionFilter();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { return  new BCryptPasswordEncoder(); }
}