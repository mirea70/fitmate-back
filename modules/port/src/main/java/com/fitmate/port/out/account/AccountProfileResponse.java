package com.fitmate.port.out.account;

import java.time.LocalDate;
import java.util.Set;

public record AccountProfileResponse(
        Long accountId,
        String loginName,
        String nickName,
        String introduction,
        Long profileImageId,
        String name,
        String phone,
        String email,
        LocalDate birthDate,
        String role,
        String gender,
        Set<Long> followings,
        Set<Long> followers
) {
}
