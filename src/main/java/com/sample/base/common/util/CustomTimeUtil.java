package com.sample.base.common.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CustomTimeUtil {
    public static Timestamp getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        return Timestamp.valueOf(now);
    }

    public static Timestamp getExpireTimeByDays(long days,boolean max) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireDate = now.plusDays(days);
        if(max){
            return Timestamp.valueOf(expireDate.with(LocalTime.MAX));
        }
        return Timestamp.valueOf(expireDate);
    }

    public static Timestamp getExpireTimeByHours(long hours) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireDate = now.plusHours(hours);
        return Timestamp.valueOf(expireDate);
    }

    public static Timestamp getExpireTimeByTime(long time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireDate = now.plusSeconds(time);
        return Timestamp.valueOf(expireDate);
    }
}
