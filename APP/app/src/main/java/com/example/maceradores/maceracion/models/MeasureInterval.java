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

    public MeasureInterval(float mainTemperature, float mainTemperatureDeviation, float pH, float phDeviation, int duration) {
        this.mainTemperature = mainTemperature;
        this.mainTemperatureDeviation = mainTemperatureDeviation;
        this.pH = pH;
        this.phDeviation = phDeviation;
        this.duration = duration;
        this.secondTemperature = -1000;
        this.secondTemperatureDeviation = -1000;
    }

    //Own Method.
    public void decrementOrder(){
        this.order = this.order - 1;
    }

    public String getDescription(){
        if( secondTemperature == -1000 || secondTemperatureDeviation == -1000 )
            return "Duración: " + this.duration + " minutos\n" +
                    "Temperatura: " + String.format("%.2f",this.mainTemperature) + "°C ± " + String.format("%.2f",this.mainTemperatureDeviation) + "°C\n" +
                    "pH: " + String.format("%.2f",this.pH) + " ± " + String.format("%.2f",this.phDeviation) + "\n" ;
        else
            return "Duración: " + this.duration + " minutos\n" +
                    "Temperatura: " + String.format("%.2f",this.mainTemperature) + "°C ± " + String.format("%.2f",this.mainTemperatureDeviation) + "°C\n" +
                    "pH: " + String.format("%.2f",this.pH) + " ± " + String.format("%.2f",this.phDeviation) + "\n" +
                    "Temperatura Decocción: " + String.format("%.2f",this.secondTemperature) + "°C ± " + String.format("%.2f",this.secondTemperatureDeviation) + "°C\n";
    }

    // Getters & Setters
    public float getMainTemperatureDeviation() {
        return mainTemperatureDeviation;
    }

    public float getSecondTemperatureDeviation() {
        return secondTemperatureDeviation;
    }

    public float getPhDeviation() {
        return phDeviation;
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

    public float getSecondTemperature() {
        return secondTemperature;
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



}
