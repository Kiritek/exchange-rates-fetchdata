package com.exchangeRates;

public class Currency {
    private String data;
    private String name;
    private double value;

    public Currency(String data, String name, double value) {
        this.data = data;
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return data + "," + name + "," + value;
    }

}
