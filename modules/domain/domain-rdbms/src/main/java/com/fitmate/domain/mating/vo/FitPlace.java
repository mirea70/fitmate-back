package com.fitmate.domain.mating.vo;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"name", "address"})
@Getter
public class FitPlace {

    private String name;

    private String address;
}
