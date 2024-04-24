package com.fitmate.domain.account.vo;

import lombok.Getter;

@Getter
public class ProfileInfo {

    private final String nickName;
    private final String introduction;
    private final Long profileImageId;

    public ProfileInfo(String nickName, String introduction, Long profileImageId) {
        this.nickName = nickName;
        this.introduction = introduction;
        this.profileImageId = profileImageId;
    }
}
