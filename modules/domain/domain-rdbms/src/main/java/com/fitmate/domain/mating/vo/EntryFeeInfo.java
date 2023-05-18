package com.fitmate.domain.mating.vo;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"max", "min"})
@Getter
public class EntryFeeInfo {
    
}
