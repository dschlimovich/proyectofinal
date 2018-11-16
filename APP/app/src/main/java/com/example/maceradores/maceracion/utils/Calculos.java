package com.example.maceradores.maceracion.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Calculos {

    public double[] calcRendTeo(double volMosto, double densEspecif, double kgMalta){
        double gradosPlato=(densEspecif-1)/0.004;
        double kgExhl = gradosPlato * densEspecif;


        double kgExtObtenido = volMosto * kgExhl/100;
        double porcRendimiento = 100 * kgExtObtenido / kgMalta;// Recordar que un 80% es un valor excelente, ya el otro 20% es bagazo

        double [] array = {kgExhl,kgExtObtenido,porcRendimiento};
        return array;
    }

    public double calcCantInsTeo(double volMosto, double densEspecif, double porcRend){
        double gradosPlato=(densEspecif-1)/0.004;
        double kgExhl = gradosPlato * densEspecif;

        double kgExtObtenido = volMosto * kgExhl/100;

        double kgGrano = kgExtObtenido * 100 / porcRend;

        return kgGrano;
    }

}