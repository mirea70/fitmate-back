package com.fitmate.domain.account.entity.vo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"nickName", "introduction", "profileImage"})
@Getter
public class ProfileInfo {
    @Column(unique = true, nullable = false)
    @Size(min = 2, max = 10)
    @NotNull
    private String nickName;

    @Column(length = 50)
    private String introduction;

    @Column
    private Long profileImage;
}
