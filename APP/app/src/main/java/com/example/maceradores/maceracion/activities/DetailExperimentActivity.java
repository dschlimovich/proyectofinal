package com.example.maceradores.maceracion.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.db.DatabaseHelper;
import com.github.mikephil.charting.charts.LineChart;

import java.util.List;

public class DetailExperimentActivity extends AppCompatActivity {

    //Data
    private int idExp;
    private String date;
    private float density;
    private List<Float> 

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

        }
        else{
            startActivity(new Intent(ExperimentActivity.this, MainActivity.class));
            finish();
        }

        //setTitle(), me pone el nombre en el ActionBar












    }





}
