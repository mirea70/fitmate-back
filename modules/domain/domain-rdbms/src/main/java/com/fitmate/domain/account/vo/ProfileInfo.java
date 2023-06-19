package com.fitmate.domain.account.vo;

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
@EqualsAndHashCode(exclude = {"nickName", "introduction"})
@Getter
public class ProfileInfo {
    @Column(unique = true, nullable = false)
    @Size(min = 2, max = 10)
    @NotNull
    @Pattern(regexp = "^[a-zA-Zㄱ-힣\\d|s]*$")
    private String nickName;

    @Column(length = 50)
    @Size(max = 50)
    private String introduction;

    @Column
    private Long profileImageId;

    public void updateProfileImageId(Long profileImageId) {
        if(profileImageId != null) this.profileImageId = profileImageId;
    }
}
