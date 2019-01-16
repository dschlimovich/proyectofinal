package com.example.maceradores.maceracion.models;

import java.util.ArrayList;
import java.util.List;

public class Experiment {
    private int id;
    private String date; //todo review formate date
    private List<SensedValues> sensedValuesList;
    private float density;

    public Experiment(){}

    public Experiment(int id, String date) {
        this.id = id;
        this.date = date;
        this.sensedValuesList = new ArrayList<SensedValues>();
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
