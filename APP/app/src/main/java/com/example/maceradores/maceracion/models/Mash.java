package com.example.maceradores.maceracion.models;

import java.util.List;

public class Mash {
    private int id;
    private String name;
    private List<MeasureInterval> plan;
    private List<Grain> grains;
    private List<Experiment> experiments;
    private int periodMeasureTemperature;
    private int periodMeasurePh;

    // Constructors

    public Mash(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Mash(int id, String name, List<MeasureInterval> plan, List<Experiment> experiments) {
        this.id = id;
        this.name = name;
        this.plan = plan;
        this.experiments = experiments;
    }

    public Mash(int id, String name, List<MeasureInterval> plan) {
        this.id = id;
        this.name = name;
        this.plan = plan;
    }

    public Mash(int id, String name, List<MeasureInterval> plan, List<Grain> grains, List<Experiment> experiments, int periodMeasureTemperature, int periodMeasurePh) {
        this.id = id;
        this.name = name;
        this.plan = plan;
        this.grains = grains;
        this.experiments = experiments;
        this.periodMeasureTemperature = periodMeasureTemperature;
        this.periodMeasurePh = periodMeasurePh;
    }

    //Own methods
    public void addMeasureInterval( MeasureInterval i){
        // I supose that this interval doesn't have the field order.
        // then i assigned the length of the list plus one.
        i.setOrder( this.plan.size() + 1 );
        plan.add(i);
    }

    // Getters & Setters
    public List<Experiment> getExperiments() {
        return experiments;
    }

    public void setExperiments(List<Experiment> experiments) {
        this.experiments = experiments;
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

    public List<Grain> getGrains() {
        return grains;
    }

    public void setGrains(List<Grain> grains) {
        this.grains = grains;
    }

    public int getPeriodMeasureTemperature() {
        return periodMeasureTemperature;
    }

    public void setPeriodMeasureTemperature(int periodMeasureTemperature) {
        this.periodMeasureTemperature = periodMeasureTemperature;
    }

    public int getPeriodMeasurePh() {
        return periodMeasurePh;
    }

    public void setPeriodMeasurePh(int periodMeasurePh) {
        this.periodMeasurePh = periodMeasurePh;
    }
}
