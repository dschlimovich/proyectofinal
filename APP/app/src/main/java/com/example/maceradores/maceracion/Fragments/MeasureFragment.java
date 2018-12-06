package com.example.maceradores.maceracion.Fragments;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.db.DatabaseHelper;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeasureFragment extends Fragment {
    private TextView tvMeasureTemp;
    private TextView tvMeasurePh;
    private Chronometer chronometer;
    //---Handler---
    Handler mHandlerThread;
    private static final int START_PROGRESS = 100;
    private static final int UPDATE_COUNT = 101;
    Thread thread1;


    public MeasureFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_measure, container, false);
        final int idMash = getArguments().getInt("idMash"); //Me traigo el idMash q viene del viewpager
        //---Thread con Handler
        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                int counter =0;

                int intervaloMedicion = intervaloMedicion(idMash);
                int NumberOfCalls = cantMediciones(idMash, intervaloMedicion);
                int sleep = (intervaloMedicion/2)*1000;

                while (counter < NumberOfCalls) {
                    //Log.d("I",":"+i);
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    Message message = new Message();
                    message.what = UPDATE_COUNT;
                    //message.arg1 = i;
                    mHandlerThread.sendMessage(message);
                    counter++;
                }
            //-------Aca va tmb lo de la verificacion para las NOTIFICACIONES DE DESVIOS!
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.tvMeasureTemp = (TextView) getView().findViewById(R.id.textViewMeasureTemp); //podria estar en el oncreate.
        this.tvMeasurePh = (TextView) getView().findViewById(R.id.textViewMeasurePh);

        loadTemperatureCardView(68, 0, 68, 3, 68, 68, 68, 68);
        loadPhCardView(5.4f, 0.1f, 5.5f, 0.2f, 25);



        chronometer = getView().findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime()); //esto debería ser el tiempo en el que hice la inserción o que tuve la primer medida.
        chronometer.start();

        //----Handler para manejo de mensajes con el thread
        mHandlerThread = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == START_PROGRESS){
                    thread1.start();
                }
                else if(msg.what == UPDATE_COUNT){
                    textView.setText("Count"+msg.arg1);
                }
            }
        };


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

    private int cantMediciones( int idMash, int intervaloMedicion){
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(duracion) FROM Intervalo WHERE maceracion = ?", new String[]{String.valueOf(idMash)});
        int duracionTotal = 0;
        if(c.moveToFirst()){
            duracionTotal = c.getInt(0);
        }
        c.close();
        db.close();
        return (duracionTotal * 60) / (intervaloMedicion/2);
    }

    private int intervaloMedicion (int idMash){
        int intervaloMedicion = 0;
        // saber el periodo de medicion.
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {"intervaloMedTemp"};
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(idMash)};

        Cursor cursor = db.query("Maceracion", columns, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()){
            intervaloMedicion = cursor.getInt(0); //como tengo una sola columna, devuelvo la primera nomas.
        }
        cursor.close();
        db.close();
        if(intervaloMedicion<30)intervaloMedicion=30; // Minimo Intervalo de Medicion es de 30 seg

        return intervaloMedicion;
    }

    public void sendNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getContext(), "default")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1, notification.build());
    }

}
