package com.fitmate.domain.mating.domain.vo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"name", "address"})
@Getter
public class FitPlace {
    @Column(name = "fit_place_name", nullable = false)
    private String name;
    @Column(name = "fit_place_address", nullable = false)
    private String address;
}
