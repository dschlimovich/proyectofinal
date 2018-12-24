package com.example.maceradores.maceracion.models;

import com.example.maceradores.maceracion.utils.Calculos;

public class Grain {
    private String name;
    private float quantity;
    private float extractPotential;

    public Grain(String name, float quantity, float extractPotential) {
        this.name = name;
        this.quantity = quantity;
        this.extractPotential = extractPotential;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getExtractPotential() {
        return extractPotential;
    }

    public void setExtractPotential(float extractPotential) {
        this.extractPotential = extractPotential;
    }

    public String getStringPlanning(){
        return  getName() +
                "\t Porcentaje: " + String.valueOf(getQuantity()*100) +"%" +
                "\t Extracto: " + String.valueOf(getExtractPotential());
    }

    public float getMaltTheoritical(float densEspecif, float volLitros, float rendEquipo){
        // densidad aca es la que planificaste
        double kgTeorico = Calculos.calcCantInsumoTeoRayDaniels(densEspecif, volLitros, this, rendEquipo);
        kgTeorico = kgTeorico - kgTeorico % 0.01; // despues dos comas decimales lo corto.

        return (float) kgTeorico;
    }

    public String getStringPlanned(float densEspecif, float volLitros, float rendEquipo){
        double kgTeorico = getMaltTheoritical( densEspecif,  volLitros, 0.7f);
        double kgPractico = getMaltTheoritical( densEspecif,  volLitros, rendEquipo);

        if(rendEquipo == 0.7f){
            return getName() +
                    "\t Cant. Teór.: " + String.valueOf((float)kgTeorico) + "kg ";
        } else{
            return getName() +
                    "\t Cant. Teór.: " + String.valueOf((float)kgTeorico) + "kg " +
                    " Práct.: " + String.valueOf((float)kgPractico) + "kg";
        }

    }

}
