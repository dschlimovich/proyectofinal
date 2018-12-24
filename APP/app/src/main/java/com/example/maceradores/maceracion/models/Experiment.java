package com.example.maceradores.maceracion.models;

import java.util.ArrayList;
import java.util.List;

public class Experiment {
    private int id;
    private String date; //todo review formate date
    private List<SensedValues> sensedValuesList;

    public Experiment(int id, String date) {
        this.id = id;
        this.date = date;
        this.sensedValuesList = new ArrayList<SensedValues>();
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

    public List<SensedValues> getSensedValuesList() {
        return sensedValuesList;
    }

    public void setSensedValuesList(List<SensedValues> sensedValuesList) {
        this.sensedValuesList = sensedValuesList;
    }
}
