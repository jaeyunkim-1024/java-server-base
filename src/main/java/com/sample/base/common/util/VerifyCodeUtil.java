package com.sample.base.common.util;

import java.security.SecureRandom;

public class VerifyCodeUtil {
    public static String generated(){
        // SecureRandom 객체 생성
        SecureRandom secureRandom = new SecureRandom();

        // 6자리 랜덤 숫자 생성
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            // 1~9 사이의 랜덤 숫자 생성
            int digit = secureRandom.nextInt(9) + 1; // 0~8 -> 1~9
            randomNumber.append(digit);
        }
        return randomNumber.toString();
    }

    public static long getVerifyExpire(){
        return 300L;
    }
}
