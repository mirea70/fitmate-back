package com.fitmate.domain.account.vo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Password {
    @Column(name = "password", nullable = false)
    @Size(min = 8)
    private String value;

    public boolean match(String password) {
        return this.value.equals(password);
    }
}
