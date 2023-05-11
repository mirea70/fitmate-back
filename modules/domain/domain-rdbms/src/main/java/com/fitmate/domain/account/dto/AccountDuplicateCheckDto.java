package com.fitmate.domain.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AccountDuplicateCheckDto {
    private String nickName;

    private String name;

    private String email;

    private String phone;
}
