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
        this.tvMeasureTemp = (TextView) getView().findViewById(R.id.textViewMeasureTemp);

        tvMeasureTemp.append(" Calculado: ");
        tvMeasureTemp.append("68"); //este debería ser el valor promedio calculado.

        tvMeasureTemp.append(" °C \t\t  Desvío: "); //esto es por una cuestión estetica.
        tvMeasureTemp.append("2"); //valor desviado respecto a lo planificado.

        tvMeasureTemp.append(" °C \n Planificado: ");
        tvMeasureTemp.append("68"); //Aca iría el valor planificado.

        tvMeasureTemp.append(" °C \t\t Alerta: ± ");
        tvMeasureTemp.append("3"); //aca sería valor de desvio

        tvMeasureTemp.append(" °C \n Sensor 1: ");
        tvMeasureTemp.append("68"); //valor del primer sensor.

        tvMeasureTemp.append(" °C \t\t\t Sensor 3: ");
        tvMeasureTemp.append("68");

        tvMeasureTemp.append(" °C \n Sensor 2: ");
        tvMeasureTemp.append("68");

        tvMeasureTemp.append(" °C \t\t\t Sensor 4: ");
        tvMeasureTemp.append("68");
        tvMeasureTemp.append(" °C");


        chronometer = getView().findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime()); //esto debería ser el tiempo en el que hice la inserción o que tuve la primer medida.
        chronometer.start();
    }
}
