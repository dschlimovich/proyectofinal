package com.example.maceradores.maceracion.models;

import java.util.List;

public class Mash {
    private int id;
    private String name;
    private List<MeasureInterval> plan;
    private List<Experience> experiences;

    public Mash(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Mash(int id, String name, List<MeasureInterval> plan, List<Experience> experiences) {
        this.id = id;
        this.name = name;
        this.plan = plan;
        this.experiences = experiences;
    }

    public Mash(int id, String name, List<MeasureInterval> plan) {
        this.id = id;
        this.name = name;
        this.plan = plan;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
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

    public List<MeasureInterval> getPlan() {
        return plan;
    }

    public void setPlan(List<MeasureInterval> plan) {
        this.plan = plan;
    }
}
