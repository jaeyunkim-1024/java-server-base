package com.sample.base.security.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sample.base.common.config.DotEnvScheme;
import com.sample.base.common.dto.JwtTokenModel;
import com.sample.base.common.util.CustomTimeUtil;
import com.sample.base.redis.service.RedisService;
import com.sample.base.security.model.CustomUserDetails;
import com.sample.base.user.enums.UserRoles;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private final RedisService redisService;
    private final Dotenv dotenv;
//    @Value("${jwt.secretKey}")
//    private String secretKey;
//
//    @Value("${jwt.expirationTime}")
//    private Long expirationTime;
//
//    @Value("${jwt.salt.value}")
//    private String salt;
//
//    @Value("${jwt.salt.num}")
//    private int num;

    /// 토큰 생성
    public JwtTokenModel issuedToken(Authentication authentication) throws JsonProcessingException {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return tokenBuilder(authentication.getName(),principal,authorities);
    }
    public JwtTokenModel reIssuedToken(String subject, CustomUserDetails principal, String authorities) throws JsonProcessingException {
        return tokenBuilder(subject,principal,authorities);
    }
    private JwtTokenModel tokenBuilder(String subject, CustomUserDetails principal, String authorities) {
        Timestamp issuedAt = CustomTimeUtil.getCurrentTime();
        String envJwtExpiration = dotenv.get(DotEnvScheme.JWT_EXPIRATION.name());
        int expirationInt = Integer.parseInt(envJwtExpiration);
        Timestamp expiration = CustomTimeUtil.getExpireTimeByTime(expirationInt);

        String jwt = Jwts.builder()
                .subject(subject)
                .claim("authorities",authorities)
                .claim("principal", principal.getUsername())
                .claim("isEmailCert",principal.isEmailCert() ? "Y" : "N")
                .claim("isLock",principal.isAccountLocked() ? "Y" : "N")
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();
        String saltingToken = salting(jwt);

        redisService.setEnableToken(saltingToken, principal.getUsername(), expirationInt);

        return JwtTokenModel.builder()
                .token(saltingToken)
                .iat(issuedAt.getTime())
                .exp(expiration.getTime())
                .build();
    }

    /// 토큰 만료
    public boolean tokenExpire(String email) {
        try{
            redisService.setDisableToken(email);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /// 토큰 추출
    public Authentication getAuthentication(String token) throws IOException {
        Claims claims = getClaimsFromToken(token);
        String email = claims.getSubject();
        String authorities = claims.get("authorities", String.class);
        return new UsernamePasswordAuthenticationToken(email, "", List.of(new SimpleGrantedAuthority(authorities)));
    }

    /// 토큰 가져오기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String saltingToken = bearerToken.substring(7);
            return desalting(saltingToken);
        }
        return null;
    }

    /// 토큰 검증
    public boolean validateToken(String token) {
        String saltingToken = salting(token);
        boolean isExpireInRedis = redisService.isEnableToken(saltingToken);
        if(!isExpireInRedis){
            return false;
        }
        try{
            Claims claims = getClaimsFromToken(token);
            Timestamp now = CustomTimeUtil.getCurrentTime();
            boolean isLock = claims.get("isLock", String.class).equals("Y");
            long issuedAt = claims.getIssuedAt().getTime();

            boolean isExpire = claims.getExpiration().before(now);
            boolean isEnable = now.getTime() > issuedAt;
            return !isLock && !isExpire && isEnable;
        }catch(SecurityException | MalformedJwtException |
               ExpiredJwtException | UnsupportedJwtException |
               IllegalArgumentException e){
            throw new JwtException(e.getClass().getName());
        }catch (Exception e){
            throw new JwtException(e.getMessage());
        }
    }

    public boolean isCertUser(HttpServletRequest request){
        String token = resolveToken(request);
        Claims claims = getClaimsFromToken(token);
        String isEmailCert = claims.get("isEmailCert", String.class);
        String authorities = claims.get("authorities", String.class);
        return isEmailCert.equals("Y") && !authorities.equals(UserRoles.NO_CERT.getType());
    }

    /// Principal(=email=subject) 가져오기
    public String getPrincipal(HttpServletRequest request){
        String token = resolveToken(request);
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    private SecretKey getKey() {
        String secretKey = dotenv.get(DotEnvScheme.JWT_SECRETKEY.name());
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String salting(String jwt){
        int num = Integer.parseInt(dotenv.get(DotEnvScheme.JWT_NUM.name()));
        String salt = dotenv.get(DotEnvScheme.JWT_SALT.name());

        String[] arr = jwt.split("\\.");
        StringBuilder stringBuilder = new StringBuilder();
        String header = arr[0];
        String payload = arr[1];
        String saltingPayload = payload.substring(0,num) + salt + payload.substring(num);
        String sign = arr[2];
        stringBuilder.append(header);
        stringBuilder.append(".");
        stringBuilder.append(saltingPayload);
        stringBuilder.append(".");
        stringBuilder.append(sign);
        return stringBuilder.toString();
    }

    private String desalting(String token){
        int num = Integer.parseInt(dotenv.get(DotEnvScheme.JWT_NUM.name()));
        String salt = dotenv.get(DotEnvScheme.JWT_SALT.name());

        String[] arr = token.split("\\.");
        StringBuilder stringBuilder = new StringBuilder();
        String header = arr[0];
        String saltingPayload = arr[1];
        String payload = saltingPayload.substring(0,num) + saltingPayload.substring(num + salt.length());
        String sign = arr[2];
        stringBuilder.append(header);
        stringBuilder.append(".");
        stringBuilder.append(payload);
        stringBuilder.append(".");
        stringBuilder.append(sign);
        return stringBuilder.toString();
    }
}
