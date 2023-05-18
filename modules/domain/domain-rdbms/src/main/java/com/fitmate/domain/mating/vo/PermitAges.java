package com.fitmate.domain.mating.vo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"max", "min"})
@Getter
public class PermitAges {

    @Column(nullable = false)
    @Max(value = 50)
    private Integer max;
    @Column(nullable = false)
    @Min(value = 15)
    private Integer min;

}
