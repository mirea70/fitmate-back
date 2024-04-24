package com.fitmate.domain.account.vo;

import lombok.Getter;

@Getter
public class PrivateInfo {
    private String name;
    private String phone;
    private String email;

    public PrivateInfo(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}
