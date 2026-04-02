package com.fitmate.domain.account;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PrivateInfo {
    private String name;
    private String phone;
    private String email;
    private LocalDate birthDate;

    public PrivateInfo(String name, String phone, String email, LocalDate birthDate) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
    }
}
