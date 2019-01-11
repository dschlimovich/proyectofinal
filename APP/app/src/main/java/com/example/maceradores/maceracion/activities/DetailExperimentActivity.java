package com.example.maceradores.maceracion.activities;

import android.arch.persistence.room.Database;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.db.DatabaseHelper;
import com.example.maceradores.maceracion.models.SensedValues;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;

public class DetailExperimentActivity extends AppCompatActivity {

    //Data
    private int idExp;
    private String date;
    private float density;
    private List<SensedValues> sensedValuesList;
    private int intervaloTemp;

    //Widgets
    private LineChart tempChart;
    private LineChart phChart;
    private LineChart EnzymesChart;
    private TextView tv_DE_date;
    private TextView tv_DE_density;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_experiment);

        Intent intent = getIntent();
        if(intent.hasExtra("idExp")){
            this.idExp = intent.getIntExtra("idExp", -1);
            sensedValuesList = getSensedValuesList(this.idExp);
            intervaloTemp = getIntervaloTemp(idExp);
            density = getDensity(idExp);

        }
        else{
            startActivity(new Intent(DetailExperimentActivity.this, MainActivity.class));
            finish();
        }

        //setTitle(), me pone el nombre en el ActionBar












    } //end onCreate

    private float getDensity(int idExp) {
        float density;
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        density = dbHelper.getDensity(idExp);
        dbHelper.close();
        return density;
    }

    private List<SensedValues> getSensedValuesList(int idExp){
        List<SensedValues> list = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        list = dbHelper.getAllSensedValues(idExp);
        dbHelper.close();
        return list;
    }

    private int getIntervaloTemp(int idExp){
        int intervalo = -1;
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int idMash = dbHelper.getIdMash(idExp);
        intervalo = dbHelper.getIntervaloMedicionTemp(idMash);
        dbHelper.close();
        return intervalo;
    }




}
