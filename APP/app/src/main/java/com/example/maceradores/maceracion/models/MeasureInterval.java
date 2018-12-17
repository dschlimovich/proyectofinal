package com.example.maceradores.maceracion.models;

public class MeasureInterval {
    private int order;
    private float mainTemperature;
    private float mainTemperatureDeviation;
    private float secondTemperature; //this is for "decocción"
    private float secondTemperatureDeviation;
    private float pH;
    private float phDeviation;
    private int duration; //would it be time or quantity of measures?

    //Constructos
    public MeasureInterval(int order, float mainTemperature, float mainTemperatureDeviation, float secondTemperature, float secondTemperatureDeviation, float pH, float phDeviation, int duration) {
        this.order = order;
        this.mainTemperature = mainTemperature;
        this.mainTemperatureDeviation = mainTemperatureDeviation;
        this.secondTemperature = secondTemperature;
        this.secondTemperatureDeviation = secondTemperatureDeviation;
        this.pH = pH;
        this.phDeviation = phDeviation;
        this.duration = duration;
    }

    public MeasureInterval(float mainTemperature, float mainTemperatureDeviation, float secondTemperature, float secondTemperatureDeviation, float pH, float phDeviation, int duration) {
        // Without set of order.
        this.mainTemperature = mainTemperature;
        this.mainTemperatureDeviation = mainTemperatureDeviation;
        this.secondTemperature = secondTemperature;
        this.secondTemperatureDeviation = secondTemperatureDeviation;
        this.pH = pH;
        this.phDeviation = phDeviation;
        this.duration = duration;
    }

    //Own Method.
    public void decrementOrder(){
        this.order = this.order - 1;
    }

    public String getDescription(){
        return "Duración: " + this.duration + " minutos\n" +
                "Temperatura: " + this.mainTemperature + "°C ± " + this.mainTemperatureDeviation + "\n" +
                "pH: " + this.pH + " ± " + this.phDeviation + "\n" +
                "Temperatura Decocción: " + this.secondTemperature + "°C ± " + this.secondTemperatureDeviation + "\n";
    }

    // Getters & Setters
    public float getMainTemperatureDeviation() {
        return mainTemperatureDeviation;
    }

    public void setMainTemperatureDeviation(float mainTemperatureDeviation) {
        this.mainTemperatureDeviation = mainTemperatureDeviation;
    }

    public float getSecondTemperatureDeviation() {
        return secondTemperatureDeviation;
    }

    public void setSecondTemperatureDeviation(float secondTemperatureDeviation) {
        this.secondTemperatureDeviation = secondTemperatureDeviation;
    }

    public float getPhDeviation() {
        return phDeviation;
    }

    public void setPhDeviation(float phDeviation) {
        this.phDeviation = phDeviation;
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

}
