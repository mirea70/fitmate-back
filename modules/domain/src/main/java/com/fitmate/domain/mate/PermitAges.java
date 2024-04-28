package com.fitmate.domain.mate;

import lombok.Getter;

@Getter
public class PermitAges {
    private Integer max;
    private Integer min;

    public PermitAges(Integer max, Integer min) {
        if(max > 50)
            throw new IllegalArgumentException("max 값은 50이하여야합니다.");
        if(min < 15)
            throw new IllegalArgumentException("min 값은 15이상이어야합니다.");

        this.max = max;
        this.min = min;
    }

    protected void update(Integer max, Integer min) {
        if(max != null) {
            if(max > 50)
                throw new IllegalArgumentException("max 값은 50이하여야합니다.");
            this.max = max;
        }
        if(min != null) {
            if(min > 50)
                throw new IllegalArgumentException("min 값은 15이상이어야합니다.");
            this.min = min;
        }
    }
}
