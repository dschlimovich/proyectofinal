package com.example.maceradores.maceracion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.models.Grain;

import java.util.List;

public class GrainListAdapter extends BaseAdapter {
    private Context context;
    private List<Grain> grainList;
    private int layout;
    private boolean planned = false;

    public GrainListAdapter(Context context, List<Grain> grainList, int layout) {
        this.context = context;
        this.grainList = grainList;
        this.layout = layout;
        if(grainList.size() > 0){
            planned = true;
        }
    }

    @Override
    public int getCount() {
        return grainList.size();
    }

    @Override
    public Grain getItem(int position) {
        return grainList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //que comience la fiesta. Como hago para agregarle un listener?
        LayoutInflater inflater = LayoutInflater.from(this.context);
        convertView = inflater.inflate(this.layout, null);

        TextView grainDetail = (TextView) convertView.findViewById(R.id.textViewGrainDetail);
        /*String detail = this.grainList.get(position).getName() + "\t porcentaje: " +
                String.valueOf(this.grainList.get(position).getQuantity()) + "\t extracto: " +
                String.valueOf(this.grainList.get(position).getExtractPotential());
        */
        String detail;
        if( planned){
            detail = grainList.get(position).getStringPlanning();
        } else{
            detail = grainList.get(position).getStringPlanning();
        }

        grainDetail.setText(detail);

        return convertView;
    }
}
