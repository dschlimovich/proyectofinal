package com.example.maceradores.maceracion.models;

import java.util.Date;

public class SensedValues {

    private int id;
    private Date date; //the format YYYY MM DD hh:mm
    //temperatures from sensors
    private float temp1;
    private float temp2;
    private float temp3;
    private float temp4;
    private float tempSecondary;//decoccion
    private float tempPH;
    // DHT11/22
    private float humidity;
    private float tempEnviroment;
    //ph sensor
    private float pH;

    public SensedValues(int id, Date date, float temp1, float temp2, float temp3, float temp4, float tempSecondary, float tempPH, float humidity, float tempEnviroment, float pH) {
        this.id = id;
        this.date = date;
        this.temp1 = temp1;
        this.temp2 = temp2;
        this.temp3 = temp3;
        this.temp4 = temp4;
        this.tempSecondary = tempSecondary;
        this.tempPH = tempPH;
        this.humidity = humidity;
        this.tempEnviroment = tempEnviroment;
        this.pH = pH;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getTemp1() {
        return temp1;
    }

    public void setTemp1(float temp1) {
        this.temp1 = temp1;
    }

    public float getTemp2() {
        return temp2;
    }

    public void setTemp2(float temp2) {
        this.temp2 = temp2;
    }

    public float getTemp3() {
        return temp3;
    }

    public void setTemp3(float temp3) {
        this.temp3 = temp3;
    }

    public float getTemp4() {
        return temp4;
    }

    public void setTemp4(float temp4) {
        this.temp4 = temp4;
    }

    public float getTempSecondary() {
        return tempSecondary;
    }

    public void setTempSecondary(float tempSecondary) {
        this.tempSecondary = tempSecondary;
    }

    public float getTempPH() {
        return tempPH;
    }

    public void setTempPH(float tempPH) {
        this.tempPH = tempPH;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getTempEnviroment() {
        return tempEnviroment;
    }

    public void setTempEnviroment(float tempEnviroment) {
        this.tempEnviroment = tempEnviroment;
    }

    public float getpH() {
        return pH;
    }

    public void setpH(float pH) {
        this.pH = pH;
    }
}
