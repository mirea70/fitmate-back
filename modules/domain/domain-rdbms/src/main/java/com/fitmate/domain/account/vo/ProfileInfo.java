package com.fitmate.domain.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class ProfileInfo {

    @Column(unique = true, nullable = false)
    @Size(min = 2, max = 10)
    @NotNull
    @Pattern(regexp = "^[a-zA-Zㄱ-힣\\d|s]*$")
//    @Schema(description = "닉네임", example = "자두")
    private String nickName;

    @Column(length = 50)
    @Size(max = 50)
//    @Schema(description = "자기소개", example = "안녕하세요. 자두입니다.")
    private String introduction;

    @Column
//    @Schema(description = "프로필 이미지ID(넣지말기)", example = "-99999")
    private Long profileImageId;

    public void updateProfileImageId(Long profileImageId) {
        if(profileImageId != null) this.profileImageId = profileImageId;
    }

    public void modifyProfileInfo(ProfileInfo requestProfileInfo) {
        if(requestProfileInfo.getNickName() != null) {
            this.nickName = requestProfileInfo.getNickName();
        }
        if(requestProfileInfo.getIntroduction() != null) {
            this.introduction = requestProfileInfo.getIntroduction();
        }
        if(requestProfileInfo.getProfileImageId() != null) {
            this.profileImageId = requestProfileInfo.getProfileImageId();
        }
    }
}
