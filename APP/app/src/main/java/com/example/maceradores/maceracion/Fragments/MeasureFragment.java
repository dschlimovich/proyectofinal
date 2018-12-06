package com.example.maceradores.maceracion.Fragments;


import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maceradores.maceracion.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeasureFragment extends Fragment {
    private TextView tvMeasureTemp;
    private TextView tvMeasurePh;
    private TextView tvMeasureEnzyme;
    private TextView tvMeasureEnviroment;
    private Chronometer chronometer;

    public MeasureFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measure, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.tvMeasureTemp = (TextView) getView().findViewById(R.id.textViewMeasureTemp); //podria estar en el oncreate.
        this.tvMeasurePh = (TextView) getView().findViewById(R.id.textViewMeasurePh);
        this.tvMeasureEnzyme = (TextView) getView().findViewById(R.id.textViewMeasureEnzyme);
        this.tvMeasureEnviroment = (TextView) getView().findViewById(R.id.textViewMeasureEnviroment);

        loadTemperatureCardView(68, 0, 68, 3, 68, 68, 68, 68);
        loadPhCardView(5.4f, 0.1f, 5.5f, 0.2f, 25);
        loadEnzymeCardView(80,0,10,0);
        loadEnviromentCardView(22,67);

        chronometer = getView().findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime()); //esto debería ser el tiempo en el que hice la inserción o que tuve la primer medida.
        chronometer.start();
    }

    private void loadEnviromentCardView(float temp, float humidity){
        //android:text="22°C \t Humedad: 67%"
        tvMeasureEnviroment.append(" Temperatura: ");
        tvMeasureEnviroment.append(String.valueOf(temp));

        tvMeasureEnviroment.append(" °C \\t Humedad: ");
        tvMeasureEnviroment.append(String.valueOf(humidity));
        tvMeasureEnviroment.append("%");
    }

    private void loadEnzymeCardView(float alfa, float proteasa, float beta, float glucanasa){
        tvMeasureEnzyme.append(" Alfa Amilasa: ");
        tvMeasureEnzyme.append(String.valueOf(alfa));

        tvMeasureEnzyme.append("% \t\t Proteasa: ");
        tvMeasureEnzyme.append(String.valueOf(proteasa));

        tvMeasureEnzyme.append("% \n Beta Amilasa: ");
        tvMeasureEnzyme.append(String.valueOf(beta));

        tvMeasureEnzyme.append("% \t\t Beta Glucanasa: ");
        tvMeasureEnzyme.append(String.valueOf(glucanasa));
        tvMeasureEnzyme.append("%");
    }

    private void loadPhCardView(float ph, float desvioObtenido, float phPlanificado, float desvioPlanificado, float tempPh) {
        //android:text=" \n Temperatura de Medición: 25°C"
        tvMeasurePh.append(" Actual: ");
        tvMeasurePh.append(String.valueOf(ph));

        tvMeasurePh.append(" \t\t\t\t\t\t Desvío: ");
        tvMeasurePh.append(String.valueOf(desvioObtenido));

        tvMeasurePh.append(" \n Planificado: ");
        tvMeasurePh.append(String.valueOf(phPlanificado));

        tvMeasurePh.append(" \t\t\t\t Alerta: ± ");
        tvMeasurePh.append(String.valueOf(desvioPlanificado));

        tvMeasurePh.append(" \n Temperatura de Medición: ");
        tvMeasurePh.append(String.valueOf(tempPh));
        tvMeasurePh.append(" °C");
    }

    private void loadTemperatureCardView(float tPromedio, float desvioObtenido, float tPlanificada, float desvioPlanificado, float t1, float t2, float t3, float t4) {
        tvMeasureTemp.append(" Calculado: ");
        tvMeasureTemp.append(String.valueOf(tPromedio)); //este debería ser el valor promedio calculado.

        tvMeasureTemp.append(" °C \t\t Desvío: "); //esto es por una cuestión estetica.
        tvMeasureTemp.append(String.valueOf(desvioObtenido)); //valor desviado respecto a lo planificado.

        tvMeasureTemp.append(" °C \n Planificado: ");
        tvMeasureTemp.append(String.valueOf(tPlanificada)); //Aca iría el valor planificado.

        tvMeasureTemp.append(" °C \t\t Alerta: ± ");
        tvMeasureTemp.append(String.valueOf(desvioPlanificado)); //aca sería valor de desvio

        tvMeasureTemp.append(" °C \n Sensor 1: ");
        tvMeasureTemp.append(String.valueOf(t1)); //valor del primer sensor.

        tvMeasureTemp.append(" °C \t\t\t Sensor 3: ");
        tvMeasureTemp.append(String.valueOf(t3));

        tvMeasureTemp.append(" °C \n Sensor 2: ");
        tvMeasureTemp.append(String.valueOf(t2));

        tvMeasureTemp.append(" °C \t\t\t Sensor 4: ");
        tvMeasureTemp.append(String.valueOf(t4));
        tvMeasureTemp.append(" °C");
    }
}
