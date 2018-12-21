package com.example.maceradores.maceracion.Fragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.db.DatabaseHelper;
import com.example.maceradores.maceracion.models.SensedValues;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartGeneralFragment extends Fragment {

    private LineChart tempChart;
    private LineChart phChart;
    private LineChart EnzymesChart;
    private int idMash;

    public ChartGeneralFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart_general, container, false);
        final int idMash = getArguments().getInt("idMash");
        this.idMash=idMash;


        //loadCharts(idMash,view);//Carga las Graficas
        loadBoxPlot(idMash,view,0);//Grafica BoxPlot Temperatura
        loadBoxPlot(idMash,view,1);//Grafica BoxPlot pH
        //setTypeofChart(0,view);

        return view;
    }

    /*    private void setTypeofChart(int chart,View view) {
            //0 para promedio, 1 para Boxplot
            TextView tv_lChartTemp = (TextView) view.findViewById(R.id.tv_linechartTemp);
            LineChart lChartTemp = (LineChart) view.findViewById(R.id.chartTemp);
            TextView tv_lChartPh = (TextView) view.findViewById(R.id.tv_linechartPh);
            LineChart lChartPh = (LineChart) view.findViewById(R.id.chartpH);
            TextView tv_boxplotTemp = (TextView) view.findViewById(R.id.tv_boxplotchartTemp);
            CombinedChart combinedChartTemp = (CombinedChart) view.findViewById(R.id.candle_stick_chartTemp);
            TextView tv_boxplotPh = (TextView) view.findViewById(R.id.tv_boxplotchartPh);
            CombinedChart combinedChartPh = (CombinedChart) view.findViewById(R.id.candle_stick_chartPh);
            switch (chart) {
                case 0:
                    if (tv_lChartTemp.getVisibility() == View.INVISIBLE) {
                        tv_lChartTemp.setVisibility(View.VISIBLE);
                        lChartTemp.setVisibility(View.VISIBLE);
                        tv_lChartPh.setVisibility(View.VISIBLE);
                        lChartPh.setVisibility(View.VISIBLE);
                        tv_boxplotTemp.setVisibility(View.INVISIBLE);
                        combinedChartTemp.setVisibility(View.INVISIBLE);
                        tv_boxplotPh.setVisibility(View.INVISIBLE);
                        combinedChartPh.setVisibility(View.INVISIBLE);
                        case 1:
                            tv_lChartTemp.setVisibility(View.INVISIBLE);
                            lChartTemp.setVisibility(View.INVISIBLE);
                            tv_lChartPh.setVisibility(View.INVISIBLE);
                            lChartPh.setVisibility(View.INVISIBLE);
                            tv_boxplotTemp.setVisibility(View.VISIBLE);
                            combinedChartTemp.setVisibility(View.VISIBLE);
                            tv_boxplotPh.setVisibility(View.VISIBLE);
                            combinedChartPh.setVisibility(View.VISIBLE);

                    }
            }
        }*/
    private void loadCharts(int idMash,View view){
        tempChart = (LineChart) view.findViewById(R.id.chartTemp);
        phChart = (LineChart) view.findViewById(R.id.chartpH);
        EnzymesChart = (LineChart) view.findViewById(R.id.chartEnzymes);

        List<List<Double>> MeanTempAndPhAndEnzymes =  meanSetsTempPhandEnzymesAct(idMash);

        List<Entry> entriesTemp = new ArrayList<Entry>();
        List<Entry> entriespH = new ArrayList<Entry>();

        List<Entry> entriesAlfa = new ArrayList<Entry>();
        List<Entry> entriesBeta = new ArrayList<Entry>();
        List<Entry> entriesGlucan = new ArrayList<Entry>();
        List<Entry> entriesProte = new ArrayList<Entry>();

        List<Integer> intervalos = intervaloMedicionTempPh(idMash);

        for ( int x=0;x<MeanTempAndPhAndEnzymes.get(0).size();x++){

            // turn your data into Entry objects
            entriesTemp.add(new Entry(x*(intervalos.get(0)/60),(float)(double) MeanTempAndPhAndEnzymes.get(0).get(x)));//Hay Double (objeto) en el vector, y esto necesita primitiva float. Por eso el doble casteo
            entriespH.add(new Entry(x*(intervalos.get(1)/60),(float)(double) MeanTempAndPhAndEnzymes.get(1).get(x)));//Hay Double (objeto) en el vector, y esto necesita primitiva float. Por eso el doble casteo

            entriesAlfa.add(new Entry(x*(intervalos.get(0)/60),(float)(double) MeanTempAndPhAndEnzymes.get(2).get(x)));
            entriesBeta.add(new Entry(x*(intervalos.get(0)/60),(float)(double) MeanTempAndPhAndEnzymes.get(3).get(x)));
            entriesGlucan.add(new Entry(x*(intervalos.get(0)/60),(float)(double) MeanTempAndPhAndEnzymes.get(4).get(x)));
            entriesProte.add(new Entry(x*(intervalos.get(0)/60),(float)(double) MeanTempAndPhAndEnzymes.get(5).get(x)));
        }

        //DataSet objects hold data which belongs together, and allow individual styling of that data
        LineDataSet dataSetTemp = new LineDataSet(entriesTemp,"Temperatura");
        dataSetTemp.setColor(Color.RED);
        dataSetTemp.enableDashedLine(1f,1f,1f);
        dataSetTemp.setDrawFilled(true);
        dataSetTemp.setFillColor(Color.RED);


        //As a last step, you need to add the LineDataSet object (or objects) you created to a
        // LineData object. This object holds all data that is represented by a Chart instance
        // and allows further styling.
        //--Temp
        LineData lineDataTemp = new LineData(dataSetTemp);

        tempChart.setData(lineDataTemp);
        tempChart.getAxisRight().setEnabled(false);

        tempChart.getDescription().setText("x:tiempo[min]; y:temperatura[ºC]");
        tempChart.getDescription().setTypeface(Typeface.DEFAULT_BOLD);
        tempChart.getDescription().setTextSize(12.0f);


        //tempChart.
        tempChart.invalidate(); //refresh

        //--pH
        LineDataSet dataSetPh = new LineDataSet(entriespH,"pH");
        dataSetPh.setColor(Color.BLUE);
        dataSetPh.enableDashedLine(1f,1f,1f);
        dataSetPh.setDrawFilled(true);
        dataSetPh.setFillColor(Color.BLUE);

        LineData lineDataPh = new LineData(dataSetPh);

        phChart.setData(lineDataPh);
        phChart.getAxisRight().setEnabled(false);
        phChart.getDescription().setText("x:tiempo[min]; y:pH[sin unidad]");
        phChart.getDescription().setTypeface(Typeface.DEFAULT_BOLD);
        phChart.getDescription().setTextSize(12.0f);
        phChart.invalidate(); //refresh

        //--Enzymes
        LineDataSet dataSetAlfa = new LineDataSet(entriesAlfa,"AlfaAmilasa");
        dataSetAlfa.setColor(Color.BLUE);

        LineDataSet dataSetBeta = new LineDataSet(entriesBeta,"BetaAmilasa");
        dataSetBeta.setColor(Color.RED);

        LineDataSet dataSetGlucan = new LineDataSet(entriesGlucan,"BetaGlucanasa");
        dataSetGlucan.setColor(Color.GREEN);

        LineDataSet dataSetProt = new LineDataSet(entriesProte,"Proteasa");
        dataSetProt.setColor(Color.MAGENTA);

        LineData lineDataEnzymes = new LineData();
        lineDataEnzymes.addDataSet(dataSetAlfa);
        lineDataEnzymes.addDataSet(dataSetBeta);
        lineDataEnzymes.addDataSet(dataSetGlucan);
        lineDataEnzymes.addDataSet(dataSetProt);

        EnzymesChart.setData(lineDataEnzymes);
        EnzymesChart.getAxisRight().setEnabled(false);
        EnzymesChart.getDescription().setText("x:tiempo[min]; y:Porcentaje de Activación[%]");
        EnzymesChart.getDescription().setTypeface(Typeface.DEFAULT_BOLD);
        EnzymesChart.getDescription().setTextSize(12.0f);



        YAxis left = EnzymesChart.getAxisLeft();
        left.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        left.setEnabled(true);
        left.setAxisMinimum(0f);
        left.setDrawGridLines(false);

        EnzymesChart.invalidate(); //refresh
    }

    private void loadBoxPlot(int idMash,View view,int temp0ph1){


            CombinedChart combinedChart = (CombinedChart) view.findViewById(R.id.candle_stick_chartTemp);

        if(temp0ph1 == 1){//Seteo grafica de pH
            combinedChart = (CombinedChart) view.findViewById(R.id.candle_stick_chartPh);
        }

        List<List<Float>> medianAndQuartils = getDataforBoxPlot(idMash,temp0ph1);
        List<CandleEntry> candleEntries = new ArrayList<CandleEntry>();
        List<Entry> entries =  new ArrayList<>();
        for(int x=0; x<medianAndQuartils.size();x++){
            candleEntries.add(new CandleEntry(x,medianAndQuartils.get(x).get(1),
                    medianAndQuartils.get(x).get(2),
                    medianAndQuartils.get(x).get(3),
                    medianAndQuartils.get(x).get(4)));

            entries.add(new Entry(x,medianAndQuartils.get(x).get(0)));
        }

        LineDataSet lineDataSet = new LineDataSet(entries,"Median");
        CandleDataSet candleDataSet = new CandleDataSet(candleEntries,"Box-plot");

        candleDataSet.setColor(Color.rgb(80, 80, 80));
        candleDataSet.setShadowColor(Color.DKGRAY);
        candleDataSet.setShadowWidth(0.4f);
        candleDataSet.setDecreasingColor(Color.RED);
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setIncreasingColor(Color.rgb(122, 242, 84));
        candleDataSet.setIncreasingPaintStyle(Paint.Style.STROKE);
        candleDataSet.setNeutralColor(Color.BLUE);
        candleDataSet.setValueTextColor(Color.RED);


        //TODO SHOW MEDIAN
        LineData lineData = new LineData(lineDataSet);
        CandleData candleData = new CandleData(candleDataSet);


        CombinedData combinedData = new CombinedData();

        combinedData.setData(lineData);
        combinedData.setData(candleData);


        //-----

        YAxis yAxis = combinedChart.getAxisLeft();
        YAxis rightAxis = combinedChart.getAxisRight();
        rightAxis.setEnabled(false);
        yAxis.setEnabled(true);
        if(temp0ph1==0) {//Solo toca el eje y si es para grafico de temperatura y no de pH
            yAxis.setAxisMinimum(20f);
            yAxis.setAxisMaximum(80f);
            yAxis.setSpaceTop(50);
        }
        else if (temp0ph1 ==1){
            yAxis.setAxisMinimum(4f);
            yAxis.setAxisMaximum(6f);
            yAxis.setSpaceTop(50);
        }

        XAxis xAxis =  combinedChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setAxisMinimum(-0.5f);


        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        combinedChart.setTouchEnabled(true);
        combinedChart.setHighlightPerDragEnabled(true);
        combinedChart.setHighlightPerTapEnabled(true);

        combinedChart.setData(combinedData);
        combinedChart.invalidate();//refresh

    }

    private List<List<Float>> getDataforBoxPlot(int idMash,int temp0ph1){
        //0=Mediana, 1=max,2=min,3=Q3 y 4=Q1.

        List<Float[][]> matrixTempPh_SV = buildSensedValueMatrix(idMash);


        //matrix[0].length gives you the number of columns (assuming all rows have the same length).
        int cols = matrixTempPh_SV.get(0)[0].length;

        List<List<Float>> columnas = new ArrayList<>();

        List<List<Float>> retorno =  new ArrayList<>();//las filas de la submatriz son la mediana y los cuartiles

        for (int j = 0; j<cols;j++){
            List<Float> column = getColumn(matrixTempPh_SV.get(temp0ph1),j);//Obtengo la columa del array como una lista
            Collections.sort(column); //Me ordena de menor a Mayor
            columnas.add(column); //Columnas para la matrix de Temp por eso get(0)

            //Hasta aca tengo las columnas ordenadas

            float max = Collections.max(column);
            float min = Collections.min(column);
            float median=0;
            float Q1=0;
            float Q3=0;
            boolean flagm=true;
            boolean flagq1=true;
            boolean flagq3=true;
            int indexM=0;
            int indexQ1=0;
            int indexQ3=0;

            boolean pair = false;
            if(column.size()%2==0){
                pair=true;
            }
            if(!pair){
                indexM = (int) Math.ceil((double) column.size() / 2);
                indexQ1 = (int) Math.ceil((double) column.size() / 4);
                indexQ3 = (int) Math.ceil((double) column.size()* 3 / 4);
            }
            else{
                indexM = column.size()/2;
                indexQ1 = column.size()/4;
                indexQ3 = column.size()*3/4;
            }
            median = column.get(indexM);
            Q1 = column.get(indexQ1-1);//-1 xq trabajo con size y los indices arrancan en cero
            Q3 = column.get(indexQ3-1);

            Log.d("mediana",String.valueOf(median));
            Log.d("min",String.valueOf(min));
            Log.d("Q1",String.valueOf(Q1));
            Log.d("Q3",String.valueOf(Q3));
            Log.d("max",String.valueOf(max));
            List<Float> cuantiles = new ArrayList<>();
            cuantiles.add(median);
            cuantiles.add(max);
            cuantiles.add(min);
            cuantiles.add(Q3);
            cuantiles.add(Q1);
            retorno.add(cuantiles);
        }
        return retorno;
    }

    private List<Float> getColumn(Float[][] matrix, int numCol){
        List<Float> retorno = new ArrayList<>();
        for (int i = 0; i<matrix.length;i++) {//Recorre por filas
            retorno.add(matrix[i][numCol]);
        }
        return retorno;

    }

    private List<List<Double>> meanSetsTempPhandEnzymesAct(int idMash) {

        List<Integer> ListidExp = getAllExperiments(idMash); //Ahora la lista viene validada.
        int NumMeasures = getMandatoryNumSensedValues(idMash);

        List<Float[][]> matrixTempPh = buildSensedValueMatrix(idMash);
        Float[][] matrizTemp = matrixTempPh.get(0);
        Float[][] matrizpH = matrixTempPh.get(1);


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

        //----Enzymes Activation
        List<Double> alphaAmylase = new ArrayList<>();
        List<Double> betaAmylase = new ArrayList<>();
        List<Double> betaGlucanase = new ArrayList<>();
        List<Double> protease = new ArrayList<>();
        for (int i=0;i<meanTemp.size();i++){
            alphaAmylase.add((double)SensedValues.alphaAmylase( (float)(double) meanTemp.get(i),
                    (float)(double)meanpH.get(i)));
            Log.d("AlfaAmilasa",String.valueOf(SensedValues.alphaAmylase( (float)(double) meanTemp.get(i),//Esto tira ceros
                    (float)(double)meanpH.get(i))));
            betaAmylase.add((double)SensedValues.betaAmylase( (float)(double) meanTemp.get(i),
                    (float)(double)meanpH.get(i)));
            Log.d("betaAmilasa",String.valueOf(SensedValues.alphaAmylase( (float)(double) meanTemp.get(i),//Esto tira ceros
                    (float)(double)meanpH.get(i))));
            betaGlucanase.add((double)SensedValues.betaGlucanase( (float)(double) meanTemp.get(i),
                    (float)(double)meanpH.get(i)));
            Log.d("BetaGlucanasa",String.valueOf(SensedValues.alphaAmylase( (float)(double) meanTemp.get(i),//Esto tira ceros
                    (float)(double)meanpH.get(i))));
            protease.add((double)SensedValues.protease( (float)(double) meanTemp.get(i),
                    (float)(double)meanpH.get(i)));
            Log.d("Proteasa",String.valueOf(SensedValues.alphaAmylase( (float)(double) meanTemp.get(i),//Esto tira ceros
                    (float)(double)meanpH.get(i))));
        }


        List<List<Double>> retorno = new ArrayList<>();
        retorno.add(meanTemp);
        retorno.add(meanpH);
        retorno.add(alphaAmylase);
        retorno.add(betaAmylase);
        retorno.add(betaGlucanase);
        retorno.add(protease);

        return retorno;
    }

    private List<Float[][]> buildSensedValueMatrix(int idMash){
        //Primero hacer un select con todos los idExp relacionados a este idMash
        List<Integer> ListidExp = getAllExperiments(idMash); //Ahora la lista viene validada.


        //Armar un Array q tenga tantas filas como idExp.
        int NumMeasures = getMandatoryNumSensedValues(idMash);
        Float[][] matrizTemp = new Float[ListidExp.size()][NumMeasures];//Tantas filas como idExp, y tantas columnas como cant de Mediciones
        Float[][] matrizpH = new Float[ListidExp.size()][NumMeasures];//Tantas filas como idExp, y tantas columnas como cant de Mediciones


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

        List<Float[][]> retorno = new ArrayList<>();
        retorno.add(matrizTemp);
        retorno.add(matrizpH);
        return  retorno;
    }

    private List<Integer> getAllExperiments(int idMash) {
        List<Integer> resultados = new ArrayList<>();

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {"id"};
        String selection = "maceracion = ? AND densidad IS NOT NULL";
        String[] selectionArgs = { String.valueOf(idMash)};

        Cursor cursor = db.query("Experimento", columns, selection, selectionArgs, null, null, null);
        List<Float> yieldList = new ArrayList<>();

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
        if(intervaloMedicion<60)intervaloMedicion=60; // Minimo Intervalo de Medicion es de 30 seg

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

    private List<Integer> intervaloMedicionTempPh (int idMash){

        // saber el periodo de medicion.
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Integer> retorno = new ArrayList<>();

        String[] columns = {"intervaloMedTemp","intervaloMedPh"};
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(idMash)};

        Cursor cursor = db.query("Maceracion", columns, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()){
            retorno.add(cursor.getInt(0)); //como tengo una sola columna, devuelvo la primera nomas.
            retorno.add(cursor.getInt(1));
        }
        cursor.close();
        db.close();

        return retorno;
    }
}
