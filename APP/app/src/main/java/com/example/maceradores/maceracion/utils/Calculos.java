package com.example.maceradores.maceracion.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Calculos {

    public double[] calcRendimiento(double volMosto, double densEspecif, double kgMalta){
        //Densidad Especifica que mido
        double gradosPlato=(densEspecif-1)/0.004;
        double kgExhl = gradosPlato * densEspecif;


        double kgExtObtenido = volMosto * kgExhl/100;
        double porcRendimiento = 100 * kgExtObtenido / kgMalta;// Recordar que un 80% es un valor excelente, ya el otro 20% es bagazo

        double [] array = {kgExhl,kgExtObtenido,porcRendimiento};
        return array;
    }

    public double calcCantInsumoPrac(double volMosto, double densEspecif, double porcRend){
        //La densidad especifica que quiero tener antes de hervir
        double gradosPlato=(densEspecif-1)/0.004;
        double kgExhl = gradosPlato * densEspecif;

        double kgExtObtenido = volMosto * kgExhl/100;// Multiplicaría esto por el porcentaje de cada tipo de grano, Ej 92% pale, 8%caramel

        double kgGrano = kgExtObtenido * 100 / porcRend;

        return kgGrano;
    }




}