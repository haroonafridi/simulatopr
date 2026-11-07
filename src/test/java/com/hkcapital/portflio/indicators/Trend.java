package com.hkcapital.portflio.indicators;

import java.util.Objects;

public class Trend {

    private String value;

    public Trend(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Trend copy() {
        return new Trend(value);
    }

    @Override
    public String toString() {
        return value;
    }
}