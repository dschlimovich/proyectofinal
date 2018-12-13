package com.example.maceradores.maceracion.utils;

import com.example.maceradores.maceracion.models.Grain;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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


    public double calcCantInsumoTeoRayDaniels(float densEspecif, float volLitros, List<Grain> granos, float rendEquipo){
        double factorDenso = (1- densEspecif)*1000; //densEspecif Objetivo!
        double ptosDensidad = factorDenso * volLitros; //Ptos de densidad de objetivo

        //1 lb = 2,2 Kg ---- 3,78 l = 1 gal ---> 1 lb/ gal = 2.2/(1/3.78) Kg/l = 8.316
        //En una proporción de 100 g, divido por 10... = 0.8316
        //PPG a PKGL en (0.1Kg/l) = PPG * 0.8316
        //ppg = ExtractoPotencial en porcentaje (Ej 0.8) por 46 q sos los ppg del azucar cuyo ExPot es 100
        //Kg de Malta = Sum (PD/EXTPOT/RENDIMIENTO/10)
        double KgdeMalta = 0;
        for(int i = 0; i < granos.size(); i++){
            double PKglx100g = 0.8316 * granos.get(i).getExtractPotential();
            double Kggrano =  (ptosDensidad * granos.get(i).getQuantity())/PKglx100g/rendEquipo/10; // RendEquipo tiene q estar como 0.8 o 0.7...
            KgdeMalta = KgdeMalta + Kggrano;
        }
        return KgdeMalta;
    }

}