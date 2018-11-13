package com.example.maceradores.maceracion.models;

public class MeasureInterval {
    private int order;
    private float mainTemperature;
    private float secondTemperature; //this is for "decocción"
    private float pH;
    private int duration; //would it be float?
    private int periodMeasureTemperature;
    private int periodMeasurePh;

    public MeasureInterval(int order, float mainTemperature, float secondTemperature, float pH, int duration, int periodMeasureTemperature, int periodMeasurePh) {
        this.order = order;
        this.mainTemperature = mainTemperature;
        this.secondTemperature = secondTemperature;
        this.pH = pH;
        this.duration = duration;
        this.periodMeasureTemperature = periodMeasureTemperature;
        this.periodMeasurePh = periodMeasurePh;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public float getMainTemperature() {
        return mainTemperature;
    }

    public void setMainTemperature(float mainTemperature) {
        this.mainTemperature = mainTemperature;
    }

    public float getSecondTemperature() {
        return secondTemperature;
    }

    public void setSecondTemperature(float secondTemperature) {
        this.secondTemperature = secondTemperature;
    }

    public float getpH() {
        return pH;
    }

    public void setpH(float pH) {
        this.pH = pH;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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
