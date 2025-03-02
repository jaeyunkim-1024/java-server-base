package com.sample.base.client.user.enums;

import lombok.Getter;

@Getter
public enum UserRoles {
    NO_CERT("NO_CERT"),
    USER("USER"),
    ADMIN("ADMIN");

    private final String type;

    UserRoles(String type){
        this.type = type;
    }
}
