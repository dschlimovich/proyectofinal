package com.example.maceradores.maceracion.models;

import java.util.Date;
import java.util.List;

public class Experiment {
    private int id;
    private Date date; //todo review formate date
    private List<SensedValues> sensedValuesList;

    public Experiment(int id, Date date, List<SensedValues> sensedValuesList) {
        this.id = id;
        this.date = date;
        this.sensedValuesList = sensedValuesList;
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

    public List<SensedValues> getSensedValuesList() {
        return sensedValuesList;
    }

    public void setSensedValuesList(List<SensedValues> sensedValuesList) {
        this.sensedValuesList = sensedValuesList;
    }
}
