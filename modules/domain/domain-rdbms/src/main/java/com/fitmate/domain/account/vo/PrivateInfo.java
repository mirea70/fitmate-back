package com.fitmate.domain.account.vo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"name", "phone", "email"})
public class PrivateInfo {

    @Column(length = 5,unique = true, nullable = false)
    @Size(min = 2, max = 5)
    private String name;

    @Column(length = 11, unique = true, nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    @Email
    @Getter
    private String email;


}
