package com.example.maceradores.maceracion.utils;

import com.example.maceradores.maceracion.models.Grain;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Calculos {

    public static double[] calcRendimiento(double volMosto, double densEspecif, double kgMalta){
        //Densidad Especifica que mido
        double gradosPlato=(densEspecif-1)/0.004;
        double kgExhl = gradosPlato * densEspecif;


        double kgExtObtenido = volMosto * kgExhl/100;
        double porcRendimiento = 100 * kgExtObtenido / kgMalta;// Recordar que un 80% es un valor excelente, ya el otro 20% es bagazo

        double [] array = {kgExhl,kgExtObtenido,porcRendimiento};
        return array;
    }

    public static double calcCantInsumoPrac(double volMosto, double densEspecif, double porcRend){
        //La densidad especifica que quiero tener antes de hervir
        double gradosPlato=(densEspecif-1)/0.004;
        double kgExhl = gradosPlato * densEspecif;

        double kgExtObtenido = volMosto * kgExhl/100;// Multiplicaría esto por el porcentaje de cada tipo de grano, Ej 92% pale, 8%caramel

        double kgGrano = kgExtObtenido * 100 / porcRend;

        return kgGrano;
    }

    public static double cantInsumoPorGrano( double kgGranoTotal, double porcentajeGrano, double extractoPotencial ){
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

    public static double temperaturaAguaInicial( double tempTarget, double tempAmb, double volAgua, double kgMalta){
        // de donde sacamos la temperatura ambiente? pinche mierda.
        double r = volAgua / kgMalta;
        return (0.41 / r) * (tempTarget - tempAmb) + tempTarget;
    }

    public static double escalonTemperatura( double tempMash, double tempTarget, double tempAmb, double volAgua, double kgMalta){
        //asi entendí la formula.
        return (tempTarget - tempAmb) * (0.41 * kgMalta + volAgua) / (tempMash - tempTarget);
    }

    public static double cantMostoRetirarDecoccion( double tempMash, double tempTarget, double volAgua){
        return ((tempTarget - tempMash) * volAgua) / (95 - tempMash);
    }

    public static double calcCantInsumoTeoRayDaniels(float densEspecif, float volLitros, Grain grano, float rendEquipo){
        double factorDenso = (densEspecif -1)*1000; //densEspecif Objetivo!
        double ptosDensidad = factorDenso * volLitros; //Ptos de densidad de objetivo

        //1 kg = 2,2 lb ---- 3,78 l = 1 gal ---> 1 lb/ gal = 2.2/(1/3.78) Kg/l = 8.316
        //En una proporción de 100 g, divido por 10... = 0.8316
        //PPG a PKGL en (0.1Kg/l) = PPG * 0.8316
        //ppg = ExtractoPotencial en porcentaje (Ej 0.8) por 46 q sos los ppg del azucar cuyo ExPot es 100
        //Kg de Malta = Sum (PD/EXTPOT/RENDIMIENTO/10)

        //double KgdeMalta = 0;
        double PKglx100g = 0.8316 * 46 * grano.getExtractPotential();
        double kgMalta =  (ptosDensidad * grano.getQuantity())/PKglx100g/rendEquipo/10; // RendEquipo tiene q estar como 0.8 o 0.7...

        //kgMalta = kgMalta - kgMalta % 0.01; //le quito lo que esta despues de las 2 cifras decimales.
        return kgMalta;
    }

    public static double cantAguaEscalon(double volPrimerEscalon, double kgMalta, List<Float> temperaturas){
        float constanteEscalon = constanteEscalon(temperaturas);
        return constanteEscalon*(0.41 * kgMalta + volPrimerEscalon);
    }

    private static float constanteEscalon(List<Float> temperaturas) {
        //primero armo las constantes.
        ArrayList<Float> desvios = new ArrayList<Float>();
        for(int i = 1; i < temperaturas.size(); i++){
            float t = ( temperaturas.get(i) - temperaturas.get(i-1)) / (99 - temperaturas.get(i));
            desvios.add(t);
        }

        return constanteRecursivaEscalon(desvios, desvios.size());
    }

    private static float constanteRecursivaEscalon(List<Float> desvios, int i) {
        if( i == 0){
            return 1;
        } else {
            float constante = 0;
            for(int j=0; j < i; j++){
                constante = constante + constanteRecursivaEscalon(desvios, j);
            }
            return desvios.get(i-1) * constante;
        }
    }

    public static double cantAguaPrimerEscalon(float volAgua, double kgMalta, List<Float> temperaturas ){
        // primero calulo la constante mistica
        float constanteEscalonada = constantePrimerEscalon(temperaturas);
        // lo que retorno aca es la cantidad de agua a utilizar.
        return (volAgua - 0.41*kgMalta*constanteEscalonada) / (1 + constanteEscalonada);
    }

    private static float constantePrimerEscalon(List<Float> temperaturas) {

        ArrayList<Float> desvios = new ArrayList<Float>();
        for(int i = 1; i < temperaturas.size(); i++){
            float t = ( temperaturas.get(i) - temperaturas.get(i-1)) / (99 - temperaturas.get(i));
            desvios.add(t);
        }

        float constante = 0; // aca se va a ir acumulando la constante.

        for(int i = 0; i < desvios.size(); i++){
            // aca tengo que llamar a la funcion recursiva magica.
            constante = constante + constanteRecursivaPrimerEscalon( desvios, i);
        }

        return constante;
    }

    private static float constanteRecursivaPrimerEscalon(List<Float> desvios, int i) {
        if( desvios.size()-1 == i ){
            return desvios.get(i);
        } else {
            return desvios.get(i) * ( 1 + constanteRecursivaPrimerEscalon(desvios, i+1));
        }
    }
}