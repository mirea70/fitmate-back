package com.fitmate.domain.mate.vo;

import lombok.Getter;

@Getter
public class FitPlace {
    private String name;
    private String address;

    public FitPlace(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
