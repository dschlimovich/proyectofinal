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

    public float getExtractPotential() {
        return extractPotential;
    }

    public String getStringPlanning(){
        return  getName() +
                "\t Porcentaje: " + String.valueOf(getQuantity()*100) +"%" +
                "\t Extracto: " + String.valueOf(getExtractPotential()*100)+"%";
    }

    public float getMaltTheoritical(float densEspecif, float volLitros, float rendEquipo){
        // densidad aca es la que planificaste
        double kgTeorico = Calculos.calcCantInsumoTeoRayDaniels(densEspecif, volLitros, this, rendEquipo);
        //kgTeorico = kgTeorico - kgTeorico % 0.01; // despues dos comas decimales lo corto.

        return (float) kgTeorico;
    }

    public float getMaltPractical(float densEspecif, float volLitros, float rendEquipo){
        double kgPractico = Calculos.calcCantInsumoPrac( volLitros, densEspecif, rendEquipo);
        //kgPractico = kgPractico - kgPractico%0.01;
        return (float) kgPractico;
    }

    public String getStringPlanned(float densEspecif, float volLitros, float rendEquipo){
        double kgTeorico = getMaltTheoritical( densEspecif,  volLitros, 0.7f);
        double kgPractico = getMaltTheoritical( densEspecif,  volLitros, rendEquipo);

        if(rendEquipo == 0.7f){
            return getName() +" "+ (getQuantity()*100)+
                    //"\t Cant. Teór.: " + String.valueOf((float)kgTeorico) + "kg ";
                    "% Cant. Teór.: " + String.format("%.2f", (float)kgTeorico) + "kg ";
        } else{
            return getName() +" "+ (getQuantity()*100)+
                    "% Cant. Teór.: " + String.format("%.2f", (float)kgTeorico) + "kg " +
                    " Práct.: " + String.format("%.2f", (float)kgPractico) + "kg";
        }

    }

}
