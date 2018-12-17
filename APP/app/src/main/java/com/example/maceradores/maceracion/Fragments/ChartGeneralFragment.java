package com.example.maceradores.maceracion.Fragments;


import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.db.DatabaseHelper;
import com.example.maceradores.maceracion.models.SensedValues;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartGeneralFragment extends Fragment {

    private LineChart tempChart;
    private LineChart phChart;
    private int idMash;

    public ChartGeneralFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grafic_general, container, false);
        final int idMash = getArguments().getInt("idMash");
        this.idMash=idMash;


        loadCharts(idMash,view);//Carga las Graficas
        return view;
    }



    private void loadCharts(int idMash,View view){
        tempChart = (LineChart) view.findViewById(R.id.chartTemp);
        phChart = (LineChart) view.findViewById(R.id.chartpH);

        List<List<Double>> MeanTempAndPh =  meanSetsTempPh(idMash);

        List<Entry> entriesTemp = new ArrayList<Entry>();
        List<Entry> entriespH = new ArrayList<Entry>();

        for ( int x=0;x<MeanTempAndPh.get(0).size();x++){

            // turn your data into Entry objects
            entriesTemp.add(new Entry(x,(float)(double) MeanTempAndPh.get(0).get(x)));//Hay Double (objeto) en el vector, y esto necesita primitiva float. Por eso el doble casteo
            entriespH.add(new Entry(x,(float)(double) MeanTempAndPh.get(1).get(x)));//Hay Double (objeto) en el vector, y esto necesita primitiva float. Por eso el doble casteo
        }

        //DataSet objects hold data which belongs together, and allow individual styling of that data
        LineDataSet dataSetTemp = new LineDataSet(entriesTemp,"Temperatura");
        dataSetTemp.setColor(Color.RED);
        dataSetTemp.enableDashedLine(0.2f,0.2f,0.2f);

        LineDataSet dataSetPh = new LineDataSet(entriespH,"pH");
        dataSetPh.setColor(Color.BLUE);

        //As a last step, you need to add the LineDataSet object (or objects) you created to a
        // LineData object. This object holds all data that is represented by a Chart instance
        // and allows further styling.
        LineData lineDataTemp = new LineData(dataSetTemp);

        tempChart.setData(lineDataTemp);
        tempChart.getAxisLeft().setEnabled(false);
        //tempChart.
        tempChart.invalidate(); //refresh

        LineData lineDataPh = new LineData(dataSetPh);

        phChart.setData(lineDataPh);
        phChart.invalidate(); //refresh
    }

    private List<List<Double>> meanSetsTempPh(int idMash) {

        //Primero hacer un select con todos los idExp relacionados a este idMash
        List<Integer> ListidExp = getAllExperiments(idMash);


        //Validacion de ListidExp
        for (int i = 0; i < ListidExp.size(); i++) {
            if(!acceptExperiment(ListidExp.get(i),idMash)){//La funcion tira true si to-do esta ok, por eso el NOT
                Log.d("IdExp invalido: ",String.valueOf(ListidExp.get(i)));
                ListidExp.remove(i);
            }
        }

        //Armar un Array q tenga tantas filas como idExp.
        int NumMeasures = getMandatoryNumSensedValues(idMash);
        float[][] matrizTemp = new float[ListidExp.size()][NumMeasures];//Tantas filas como idExp, y tantas columnas como cant de Mediciones
        float[][] matrizpH = new float[ListidExp.size()][NumMeasures];//Tantas filas como idExp, y tantas columnas como cant de Mediciones


        //Recorrer los idExp haciendo Select de Sensed Values y ubicarlos en cada Fila de la Matriz.
        for (int i = 0; i < ListidExp.size(); i++) {
            List<SensedValues> sensedValues = getSensedValues(ListidExp.get(i));
            for (int j = 0; j < NumMeasures; j++) {
                matrizTemp[i][j] = validatedTempMean(sensedValues.get(j).getTemp1(),
                        sensedValues.get(j).getTemp2(), sensedValues.get(j).getTemp3(), sensedValues.get(j).getTemp4());
                matrizpH[i][j]=sensedValues.get(j).getpH();
                Log.d("MatrizTemp celda: "+String.valueOf(i)+","+String.valueOf(j)+"=",String.valueOf(matrizTemp[i][j]));
                Log.d("MatrizpH celda: "+String.valueOf(i)+","+String.valueOf(j)+"=",String.valueOf(matrizpH[i][j]));
            }
            //Validar que la cant de Sensed Values q vienen por IdExp sea == a los q deberia de tener un Exp completo.
        }

        //Calculo Promedio
        List<Double> meanTemp = new ArrayList<>();
        List<Double> meanpH = new ArrayList<>();
        for (int j = 0; j < NumMeasures; j++) {
            double sumaTemp = 0;
            double sumapH = 0;
            for (int i = 0; i < ListidExp.size(); i++) {
                sumaTemp = sumaTemp + matrizTemp[i][j];
                sumapH = sumapH + matrizpH[i][j];
            }
            meanTemp.add(sumaTemp/ListidExp.size());
            meanpH.add(sumapH/ListidExp.size());
            Log.d("Vector MeanTemp: "+String.valueOf(j)+"=",String.valueOf(meanTemp.get(j)));
            Log.d("Vector MeanpH: "+String.valueOf(j)+"=",String.valueOf(meanpH.get(j)));
        }

        List<List<Double>> retorno = new ArrayList<>();
        retorno.add(meanTemp);
        retorno.add(meanpH);
        return retorno;
    }

    private List<Integer> getAllExperiments(int idMash) {
        List<Integer> resultados = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Filter results WHERE "title" = 'My Title'
        //String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(this.idMash)};
        //TODO CAMBIAR RAWQUERY POR QUERY NOMAL
        Cursor cursor = db.rawQuery("SELECT id FROM Experimento  WHERE maceracion = ? ORDER BY fecha DESC", selectionArgs);

        //List itemNames = new ArrayList<>();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
            );

            resultados.add(id);
        } // end while
        cursor.close();
        dbHelper.close();
        return resultados;
    } //end getAllExperiments

    private List<SensedValues> getSensedValues(int idExp){

        List<SensedValues> listSensedValues = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //TODO CAMBIAR RAWQUERY POR QUERY COMUN
        Cursor cursor = db.rawQuery("SELECT * FROM SensedValues WHERE id_exp = ? ", new String[] {String.valueOf(idExp)});
        while(cursor.moveToNext()){
            //if(cursor.moveToFirst()){
            int id = cursor.getInt( cursor.getColumnIndexOrThrow("id"));
            int idRaspi = cursor.getInt( cursor.getColumnIndexOrThrow("idRaspi"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("fechayhora"));
            float temp1 = cursor.getFloat(cursor.getColumnIndexOrThrow("temp1"));
            float temp2 = cursor.getFloat(cursor.getColumnIndexOrThrow("temp2"));
            float temp3 = cursor.getFloat(cursor.getColumnIndexOrThrow("temp3"));
            float temp4 = cursor.getFloat(cursor.getColumnIndexOrThrow("temp4"));
            float temp5 = cursor.getFloat(cursor.getColumnIndexOrThrow("temp5"));
            float tempPh = cursor.getFloat(cursor.getColumnIndexOrThrow("tempPh"));
            float tempAmb = cursor.getFloat(cursor.getColumnIndexOrThrow("tempAmb"));
            float humidity = cursor.getFloat(cursor.getColumnIndexOrThrow("humity"));
            float pH= cursor.getFloat(cursor.getColumnIndexOrThrow("pH"));

            SensedValues sv = new SensedValues(id,idRaspi, date, temp1, temp2, temp3, temp4, temp5, tempPh, humidity, tempAmb, pH);
            listSensedValues.add(sv);
        }
        cursor.close();
        db.close();
        return listSensedValues;
    }

    private int getMandatoryNumSensedValues(int idMash) {
        // primero debería saber si ya se ejecutaron todas las mediciones planificadas.

        int cadaCuantoMido = intervaloMedicion(idMash);
        int llamadasAPI = cantMediciones( idMash, cadaCuantoMido);//Esto devuelve el doble de mediciones
        int cantMediciones = llamadasAPI/2;

        return cantMediciones;

    }

    private boolean acceptExperiment(int idExperiment, int idMash) {
        // primero debería saber si ya se ejecutaron todas las mediciones planificadas.
        int medicionesRealizadas = amountSensedValue(idExperiment);
        int cadaCuantoMido = intervaloMedicion(idMash);
        int medicionesARealizar = cantMediciones( idMash, cadaCuantoMido);
        Log.d("Mediciones a realizar: ",String.valueOf(medicionesARealizar/2));
        Log.d("Mediciones realizadas: ",String.valueOf(medicionesRealizadas));

        if( medicionesRealizadas == medicionesARealizar/2){
            return true;
        } else {
            Toast.makeText(getContext(), "El idExp="+String.valueOf(idExperiment)+" tiene menos SensedValues", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private int cantMediciones( int idMash, int intervaloMedicion){
        //TODO hacer con medicionesPorIntervalo

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

    private float validatedTempMean(float t1, float t2, float t3, float t4){
        int divisor=4;
        float dividendo=0;
        if(t1==-1000){
            divisor--;
        }else{dividendo = dividendo + t1;}
        if(t2==-1000){
            divisor--;
        }else{dividendo = dividendo + t2;}
        if(t3==-1000){
            divisor--;
        }else{dividendo = dividendo + t3;}
        if(t4==-1000){
            divisor--;
        }else{dividendo = dividendo + t4;}
        if(dividendo==0){//Asi evito la division por cero
            return -1000;//Devuelvo, una temperatura invalida
        }else {
            float promedio = dividendo / divisor;
            return promedio;
        }

    }

    public int amountSensedValue(int idExp){
        //int amount = 0;
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int amount = (int) DatabaseUtils.queryNumEntries(db, "SensedValues", "id_exp=?", new String[] {String.valueOf(idExp)});
        db.close();
        return amount;
    }
}
