package com.example.maceradores.maceracion.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.models.SensedValues;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class DetailExperimentActivity extends AppCompatActivity {

    //Data
    private int idExp;
    private String date;
    private float density;
    private List<SensedValues> sensedValuesList = new ArrayList<>();
    private int intervalo;
    private List<Float> tempProm =new ArrayList<>();
    private List<List<Float>> enzymesActivation = new ArrayList<>();

    //Widgets
    private LineChart ltempChartProm;
    private LineChart lphChart;
    private LineChart lEnzymesChart;
    private LineChart ltempChartSensor1;
    private LineChart ltempChartSensor2;
    private LineChart ltempChartSensor3;
    private LineChart ltempChartSensor4;
    private TextView tv_DE_date;
    private TextView tv_DE_density;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_experiment);

        Intent intent = getIntent();
        if(intent.hasExtra("idExp")){
            this.idExp = intent.getIntExtra("idExp", -1);

        }
        else{
            startActivity(new Intent(DetailExperimentActivity.this, ExperimentActivity.class));
            finish();
        }

        //TODO TRAER LA FECHA ANTES DE SETAR EL TITLE
        setTitle(date);//Seteo el nombre en el ActionBar


        this.tv_DE_date = (TextView)  findViewById(R.id.tv_DE_date);
        this.tv_DE_density = (TextView)  findViewById(R.id.tv_DE_densitiy);

        this.ltempChartProm = (LineChart) findViewById(R.id.DE_chartTempProm);
        this.lphChart = (LineChart) findViewById(R.id.DE_chartpH);
        this.lEnzymesChart = (LineChart) findViewById(R.id.DE_chartEnzymes);
        this.ltempChartSensor1 = (LineChart) findViewById(R.id.DE_chartTemp1);
        this.ltempChartSensor2 = (LineChart) findViewById(R.id.DE_chartTemp2);
        this.ltempChartSensor3 = (LineChart) findViewById(R.id.DE_chartTemp3);
        this.ltempChartSensor4 = (LineChart) findViewById(R.id.DE_chartTemp4);



        //todo getSensedValues

        calcTempProm(); //Calcula los valores promedio de temperatura
        this.enzymesActivation = enzymesActivation(); //esta funcion usa tempProm!!

        //Carga de graficas
        loadCharts();

        
    }

    private void loadCharts(){

        List<Entry> entriesTemp = new ArrayList<Entry>();
        List<Entry> entriespH = new ArrayList<Entry>();

        List<Entry> entriesAlfa = new ArrayList<Entry>();
        List<Entry> entriesBeta = new ArrayList<Entry>();
        List<Entry> entriesGlucan = new ArrayList<Entry>();
        List<Entry> entriesProte = new ArrayList<Entry>();

        List<Entry> entriesTemp1 = new ArrayList<Entry>();
        List<Entry> entriesTemp2 = new ArrayList<Entry>();
        List<Entry> entriesTemp3 = new ArrayList<Entry>();
        List<Entry> entriesTemp4 = new ArrayList<Entry>();


        for ( int x=0;x<sensedValuesList.size();x++){

            // turn your data into Entry objects
            entriesTemp.add(new Entry(x*(intervalo/60), tempProm.get(x)));

            entriespH.add(new Entry(x*(intervalo/60),sensedValuesList.get(x).getpH()));

            entriesAlfa.add(new Entry(x*(intervalo/60),enzymesActivation.get(0).get(x)));
            entriesBeta.add(new Entry(x*(intervalo/60),enzymesActivation.get(1).get(x)));
            entriesGlucan.add(new Entry(x*(intervalo/60),enzymesActivation.get(2).get(x)));
            entriesProte.add(new Entry(x*(intervalo/60),enzymesActivation.get(3).get(x)));

            entriesTemp1.add(new Entry(x*(intervalo/60), sensedValuesList.get(x).getTemp1()));
            entriesTemp2.add(new Entry(x*(intervalo/60), sensedValuesList.get(x).getTemp2()));
            entriesTemp3.add(new Entry(x*(intervalo/60), sensedValuesList.get(x).getTemp3()));
            entriesTemp4.add(new Entry(x*(intervalo/60), sensedValuesList.get(x).getTemp4()));

        }

        //**************************************tempProm********************************************
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

        ltempChartProm.setData(lineDataTemp);
        ltempChartProm.getAxisRight().setEnabled(false);

        ltempChartProm.getDescription().setText("x:tiempo[min]; y:temperatura[ºC]");
        ltempChartProm.getDescription().setTypeface(Typeface.DEFAULT_BOLD);
        ltempChartProm.getDescription().setTextSize(12.0f);


        //tempChart.
        ltempChartProm.invalidate(); //refresh
        //*********************************fin tempProm*********************************************


        //*************************************** pH************************************************
        LineDataSet dataSetPh = new LineDataSet(entriespH,"pH");
        dataSetPh.setColor(Color.BLUE);
        dataSetPh.enableDashedLine(1f,1f,1f);
        dataSetPh.setDrawFilled(true);
        dataSetPh.setFillColor(Color.BLUE);

        LineData lineDataPh = new LineData(dataSetPh);

        lphChart.setData(lineDataPh);
        lphChart.getAxisRight().setEnabled(false);
        lphChart.getDescription().setText("x:tiempo[min]; y:pH[sin unidad]");
        lphChart.getDescription().setTypeface(Typeface.DEFAULT_BOLD);
        lphChart.getDescription().setTextSize(12.0f);
        lphChart.invalidate(); //refresh
        //****************************************fin pH********************************************


        //**************************************Enzymes*********************************************
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

        lEnzymesChart.setData(lineDataEnzymes);
        lEnzymesChart.getAxisRight().setEnabled(false);
        lEnzymesChart.getDescription().setText("x:tiempo[min]; y:Porcentaje de Activación[%]");
        lEnzymesChart.getDescription().setTypeface(Typeface.DEFAULT_BOLD);
        lEnzymesChart.getDescription().setTextSize(12.0f);



        YAxis left = lEnzymesChart.getAxisLeft();
        left.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        left.setEnabled(true);
        left.setAxisMinimum(0f);
        left.setDrawGridLines(false);

        lEnzymesChart.invalidate(); //refresh
        //**********************************fin Enzymes*********************************************


        //*********************************temp Sensores *******************************************
        //SENSOR 1
        //DataSet objects hold data which belongs together, and allow individual styling of that data
        LineDataSet dataSetTemp1 = new LineDataSet(entriesTemp1,"Temperatura");
        dataSetTemp1.setColor(Color.RED);
        dataSetTemp1.enableDashedLine(1f,1f,1f);
        dataSetTemp1.setDrawFilled(true);
        dataSetTemp1.setFillColor(Color.RED);


        //As a last step, you need to add the LineDataSet object (or objects) you created to a
        // LineData object. This object holds all data that is represented by a Chart instance
        // and allows further styling.
        //--Temp
        LineData lineDataTemp1 = new LineData(dataSetTemp1);

        ltempChartSensor1.setData(lineDataTemp1);
        ltempChartSensor1.getAxisRight().setEnabled(false);

        ltempChartSensor1.getDescription().setText("x:tiempo[min]; y:temperatura[ºC]");
        ltempChartSensor1.getDescription().setTypeface(Typeface.DEFAULT_BOLD);
        ltempChartSensor1.getDescription().setTextSize(12.0f);


        //tempChartSensor.
        ltempChartSensor1.invalidate(); //refresh


        //SENSOR 2

        LineDataSet dataSetTemp2 = new LineDataSet(entriesTemp2,"Temperatura");
        dataSetTemp2.setColor(Color.RED);
        dataSetTemp2.enableDashedLine(1f,1f,1f);
        dataSetTemp2.setDrawFilled(true);
        dataSetTemp2.setFillColor(Color.RED);

        LineData lineDataTemp2 = new LineData(dataSetTemp1);

        ltempChartSensor2.setData(lineDataTemp2);
        ltempChartSensor2.getAxisRight().setEnabled(false);

        ltempChartSensor2.getDescription().setText("x:tiempo[min]; y:temperatura[ºC]");
        ltempChartSensor2.getDescription().setTypeface(Typeface.DEFAULT_BOLD);
        ltempChartSensor2.getDescription().setTextSize(12.0f);


        //tempChartSensor.
        ltempChartSensor2.invalidate(); //refresh

        //SENSOR 3

        LineDataSet dataSetTemp3 = new LineDataSet(entriesTemp3,"Temperatura");
        dataSetTemp3.setColor(Color.RED);
        dataSetTemp3.enableDashedLine(1f,1f,1f);
        dataSetTemp3.setDrawFilled(true);
        dataSetTemp3.setFillColor(Color.RED);

        LineData lineDataTemp3 = new LineData(dataSetTemp1);

        ltempChartSensor3.setData(lineDataTemp3);
        ltempChartSensor3.getAxisRight().setEnabled(false);

        ltempChartSensor3.getDescription().setText("x:tiempo[min]; y:temperatura[ºC]");
        ltempChartSensor3.getDescription().setTypeface(Typeface.DEFAULT_BOLD);
        ltempChartSensor3.getDescription().setTextSize(12.0f);


        //tempChartSensor.
        ltempChartSensor3.invalidate(); //refresh

        //SENSOR 4

        LineDataSet dataSetTemp4 = new LineDataSet(entriesTemp4,"Temperatura");
        dataSetTemp4.setColor(Color.RED);
        dataSetTemp4.enableDashedLine(1f,1f,1f);
        dataSetTemp4.setDrawFilled(true);
        dataSetTemp4.setFillColor(Color.RED);


        LineData lineDataTemp4 = new LineData(dataSetTemp4);

        ltempChartSensor4.setData(lineDataTemp4);
        ltempChartSensor4.getAxisRight().setEnabled(false);

        ltempChartSensor4.getDescription().setText("x:tiempo[min]; y:temperatura[ºC]");
        ltempChartSensor4.getDescription().setTypeface(Typeface.DEFAULT_BOLD);
        ltempChartSensor4.getDescription().setTextSize(12.0f);


        //tempChartSensor.
        ltempChartSensor4.invalidate(); //refresh

        //*********************************fin temp Sensores ***************************************
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
    private List<List<Float>> enzymesActivation(){
        //----Enzymes Activation
        List<Float> alphaAmylase = new ArrayList<>();
        List<Float> betaAmylase = new ArrayList<>();
        List<Float> betaGlucanase = new ArrayList<>();
        List<Float> protease = new ArrayList<>();
        for (int i=0;i<tempProm.size();i++){
            alphaAmylase.add(SensedValues.alphaAmylase( tempProm.get(i),
                    sensedValuesList.get(i).getpH()));

            betaAmylase.add(SensedValues.betaAmylase( tempProm.get(i),
                    sensedValuesList.get(i).getpH()));

            betaGlucanase.add(SensedValues.betaGlucanase( tempProm.get(i),
                    sensedValuesList.get(i).getpH()));

            protease.add(SensedValues.protease( tempProm.get(i),
                    sensedValuesList.get(i).getpH()));

        }
        List<List<Float>> retorno = new ArrayList<>();

        retorno.add(alphaAmylase);
        retorno.add(betaAmylase);
        retorno.add(betaGlucanase);
        retorno.add(protease);

        return retorno;
    }

    private void calcTempProm(){
        for(int i = 0;i<sensedValuesList.size(); i++){
            this.tempProm.add(validatedTempMean(sensedValuesList.get(i).getTemp1(),
                    sensedValuesList.get(i).getTemp2(),
                    sensedValuesList.get(i).getTemp3(),
                    sensedValuesList.get(i).getTemp4()));
        }
    }
}