package com.developer.dschlimovich.grafic;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LineChart lineChart = findViewById(R.id.lineChart);
        ArrayList<Entry> entries = new ArrayList<>();

        for (int i=0; i<40; i++) {
            entries.add(new Entry(i, i*i));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColor(Color.RED);
        dataSet.setValueTextColor(Color.GREEN);



        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
//        lineChart.getAxisLeft().setEnabled(false);
    }
}
