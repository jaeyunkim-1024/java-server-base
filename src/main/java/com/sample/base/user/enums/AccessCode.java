package com.sample.base.user.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum AccessCode {
    LOGIN_SUCCESS("10","로그인 성공"),
    LOGIN_FAILED_NOT_CORRECT_EMAIL_PWD("20","이메일 혹은 비밀번호가 잘못되었습니다."),
    LOGIN_FAILED_INVALID_PASSWORD("30","비밀번호는 최소 6자, 최대 20자를 만족해야합니다."),
    LOGOUT("40","로그아웃 성공");

    private final String code;
    private final String desc;
    AccessCode(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static Map<String,AccessCode> getAccessCodeMap(){
        Map<String,AccessCode> map = new HashMap<>();
        map.put(LOGIN_SUCCESS.getCode(),LOGIN_SUCCESS);
        map.put(LOGIN_FAILED_NOT_CORRECT_EMAIL_PWD.getCode(),LOGIN_FAILED_NOT_CORRECT_EMAIL_PWD);
        map.put(LOGIN_FAILED_INVALID_PASSWORD.getCode(),LOGIN_FAILED_INVALID_PASSWORD);
        map.put(LOGOUT.getCode(),LOGOUT);

        return map;
    }
}
