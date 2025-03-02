package com.sample.base.common.enums;

public enum RedisPrefixEnum {
    EMAIL_AUTH("EA"),
    USER_TOKEN("UT");

    public final String prefix;
    RedisPrefixEnum(String prefix){
        this.prefix = prefix;
    }
}
