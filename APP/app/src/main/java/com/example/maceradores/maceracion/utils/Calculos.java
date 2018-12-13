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

    public double cantInsumoPorGrano( double kgGranoTotal, double porcentajeGrano, double extractoPotencial ){
        // supongando porcentaje con formato 0.9 para 90% y extracto potencia en formato 0.81
        // si no estan expersados en porcentaje, lo divido por cien :)
        if(porcentajeGrano > 1){
            porcentajeGrano = porcentajeGrano / 100;
        }
        if(extractoPotencial > 1){
            extractoPotencial = extractoPotencial / 100;
        }

        return (kgGranoTotal * porcentajeGrano) / extractoPotencial;
    }

    public double temperaturaAguaInicial( double tempTarget, double tempAmb, double volAgua, double kgMalta){
        // de donde sacamos la temperatura ambiente? pinche mierda.
        double r = volAgua / kgMalta;
        return (0.41 / r) * (tempTarget - tempAmb) + tempTarget;
    }

    public double escalonTemperatura( double tempMash, double tempTarget, double tempAmb, double volAgua, double kgMalta){
        //asi entendí la formula.
        return (tempTarget - tempAmb) * (0.41 * kgMalta + volAgua) / (tempMash - tempTarget);
    }


}