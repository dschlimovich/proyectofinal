package com.example.maceradores.maceracion.models;

public class Grain {
    private String name;
    private float quantity;
    private float yield;

    public Grain(String name, float quantity, float yield) {
        this.name = name;
        this.quantity = quantity;
        this.yield = yield;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getYield() {
        return yield;
    }

    public void setYield(float yield) {
        this.yield = yield;
    }
}
