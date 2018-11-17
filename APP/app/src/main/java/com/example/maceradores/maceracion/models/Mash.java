package com.example.maceradores.maceracion.models;

import java.util.ArrayList;
import java.util.List;

public class Mash {
    private int id;
    private String name;
    private String tipo;
    private List<MeasureInterval> plan;
    private List<Grain> grains;
    private List<Experiment> experiments;
    private float volumen;
    private float densidadObjetivo;
    private int periodMeasureTemperature;
    private int periodMeasurePh;

    // Constructors

    public Mash(String name) {
        this.name = name;
    }

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

    public void removeMeasureInterval(int position){
        // i need to reset the order and delete interval.
        if( position > plan.size() || position < 0){
            //si le erro con los numeritos que no haga nada.
            return;
        }
        boolean finded = false;
        int it = plan.size() - 1; // arranco por el utlimo
        while(finded){
            if( it == position){
                // lo encontre y lo elimino.
                plan.remove(it);
                finded = true;
            }
            else{
                //agarro lo que esta en it y le resto uno al orden.
                plan.get(it).decrementOrder();
                // y sigo con el proximo.
                it --;
            }
        } //end while

    }

    // Getters & Setters

    public float getDensidadObjetivo() {
        return densidadObjetivo;
    }

    public void setDensidadObjetivo(float densidadObjetivo) {
        this.densidadObjetivo = densidadObjetivo;
    }

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
        // setteo los ordenes de la lista para que queden de manera creciente.

        this.plan = new ArrayList<MeasureInterval>();

        for(int i= 0; i < plan.size(); i++){
            addMeasureInterval(plan.get(i));
        }
        //this.plan = plan;
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

    public float getVolumen() {
        return volumen;
    }

    public void setVolumen(float volumen) {
        this.volumen = volumen;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
