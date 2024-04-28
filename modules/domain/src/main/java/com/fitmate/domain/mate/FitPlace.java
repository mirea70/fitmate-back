package com.fitmate.domain.mate;

import lombok.Getter;

@Getter
public class FitPlace {
    private String name;
    private String address;

    public FitPlace(String name, String address) {
        this.name = name;
        this.address = address;
    }

    protected void update(String name, String address) {
        if(name != null) this.name = name;
        if(address != null) this.address = address;
    }
}
