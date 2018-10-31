package com.example.maceradores.maceracion.models;

import java.util.List;

public class Mash {
    private int id;
    private String name;
    private List<MeasureValues> plan;
    private List<Experience> experiences;

    public Mash(int id, String name, List<MeasureValues> plan) {
        this.id = id;
        this.name = name;
        this.plan = plan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MeasureValues> getPlan() {
        return plan;
    }

    public void setPlan(List<MeasureValues> plan) {
        this.plan = plan;
    }
}
