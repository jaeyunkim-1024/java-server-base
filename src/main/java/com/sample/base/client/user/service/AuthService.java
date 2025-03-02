package com.sample.base.client.user.service;

import com.sample.base.client.user.dto.UserInfoDto;
import com.sample.base.client.user.dto.UserInfoJoinRequestDto;
import com.sample.base.client.user.dto.UserInfoUpdateRequestDto;
import com.sample.base.client.user.entity.LoginHistory;
import com.sample.base.client.user.entity.UserInfo;
import com.sample.base.client.user.enums.AccessCode;
import com.sample.base.client.user.enums.UserRoles;
import com.sample.base.client.user.kafka.model.EmailDto;
import com.sample.base.client.user.repository.LoginHistoryRepository;
import com.sample.base.client.user.repository.UserInfoRepository;
import com.sample.base.common.service.RedisService;
import com.sample.base.common.util.CustomTimeUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserInfoRepository userInfoRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisService redisService;

    public UserInfoDto signUp(UserInfoJoinRequestDto dto) {
        String email = dto.getEmail();
        String username = dto.getUserName();
        if(!StringUtils.hasLength(email) || !StringUtils.hasLength(username)) {
            throw new IllegalArgumentException("빈 값입니다.");
        }

        int cnt = userInfoRepository.countUserInfoByEmail(email);
        if(cnt > 0){
            throw new IllegalArgumentException("이메일 중복");
        }

        int size = dto.getPassword().length();
        if(size < 6 || size > 20){
            throw new IllegalArgumentException("비밀번호는 최소 6자, 최대 20자를 만족해야합니다.");
        }

        Timestamp createdAt = CustomTimeUtil.getCurrentTime();
        Timestamp expireAt = CustomTimeUtil.getExpireTimeByDays(90,true);

        UserInfo entity= UserInfo.builder()
                .userName(dto.getUserName())
                .email(dto.getEmail())
                .createdAt(createdAt)
                .updatedAt(createdAt)
                .expireAt(expireAt)
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build();

        return UserInfoDto.fromEntity(userInfoRepository.save(entity));
    }

    public void logout(String email){
        UserInfo entity = userInfoRepository.findUserInfoByEmail(email);
        if(entity != null){
            LoginHistory loginHistory = LoginHistory
                    .builder()
//                    .email(entity.getEmail())
                    .accessCd(AccessCode.LOGOUT.getCode())
                    .build();
            loginHistoryRepository.save(loginHistory);
        }
    }

    public int verifyCode(EmailDto dto){
        String key = dto.generateRedisKey();
        String value = dto.getVerifyCode();
        boolean isCorrectVerifyCode = redisService.isExist(key);
        String verifyCode = redisService.getValue(key);
        if(!isCorrectVerifyCode || !verifyCode.equals(value)){
            return -1;
        }
        UserInfo org = userInfoRepository.findUserInfoByEmail(dto.getEmail());
        UserInfoUpdateRequestDto updateDto = UserInfoUpdateRequestDto.builder()
                .email(dto.getEmail())
                .userRole(UserRoles.USER.getType())
                .build();

        UserInfo updateEntity = org.mergeFrom(updateDto.toEntity(org.getUserSeq()));
        userInfoRepository.save(updateEntity);
        return redisService.deleteKey(key) ? 0 : 1;
    }
}
