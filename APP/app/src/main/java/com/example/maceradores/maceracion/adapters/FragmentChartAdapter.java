package com.example.maceradores.maceracion.adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maceradores.maceracion.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class FragmentChartAdapter extends RecyclerView.Adapter<FragmentChartAdapter.ViewHolder> {
    private List<List<Float>> ListExperiments;
    private int layout;
    private float intMedTempMin;
    List<String> listDates;

    public FragmentChartAdapter(List<List<Float>> listExperiments,List<String> listDates, int layout, float intMedTempMin ) {
        this.ListExperiments = listExperiments;
        this.layout = layout;
        this.intMedTempMin=intMedTempMin;
        this.listDates=listDates;
    }

    @NonNull
    @Override
    public FragmentChartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(this.layout,viewGroup,false);
        return new FragmentChartAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(this.ListExperiments.get(i),this.listDates.get(i), intMedTempMin);
    }

    @Override
    public int getItemCount() {
        return this.ListExperiments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LineChart lineChart;
        private TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.lineChart = (LineChart) itemView.findViewById(R.id.chart);
            this.textView = (TextView) itemView.findViewById(R.id.tv_date);
        }
        public void bind(final List<Float> tempSensedValues,String date, float intMedTempMinutes){

            textView.setText("Experiment of date: " + date);
            List<Entry> entriesTemp = new ArrayList<Entry>();

            for ( int x=0;x<tempSensedValues.size();x++){
                // turn your data into Entry objects
                entriesTemp.add(new Entry(x*intMedTempMinutes, tempSensedValues.get(x)));
            }
            Log.d("SizeEntries",String.valueOf(entriesTemp.size()));

            //DataSet objects hold data which belongs together, and allow individual styling of that data
            LineDataSet dataSetTemp = new LineDataSet(entriesTemp,"Temperatura");
            dataSetTemp.setColor(Color.RED);
            dataSetTemp.enableDashedLine(1f,1f,1f);
            dataSetTemp.setDrawFilled(true);
            dataSetTemp.setFillColor(Color.RED);

            //As a last step, you need to add the LineDataSet object (or objects) you created to a
            // LineData object. This object holds all data that is represented by a Chart instance
            // and allows further styling.

            LineData lineDataTemp = new LineData(dataSetTemp);
            if(lineChart == null)Log.d("lineChart"," Esta vacío");

            this.lineChart.setData(lineDataTemp);
            this.lineChart.getAxisLeft().setEnabled(false);
            this.lineChart.getDescription().setText("x:tiempo[min]; y:temperatura[ºC]");
            this.lineChart.getDescription().setTypeface(Typeface.DEFAULT_BOLD);
            this.lineChart.getDescription().setTextSize(12.0f);

            //tempChart.
            this.lineChart.invalidate(); //refresh

        }

    }
}
