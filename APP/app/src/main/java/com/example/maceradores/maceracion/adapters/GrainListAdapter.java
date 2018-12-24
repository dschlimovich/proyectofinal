package com.example.maceradores.maceracion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.models.Grain;
import com.example.maceradores.maceracion.models.Mash;

import java.util.List;

public class GrainListAdapter extends BaseAdapter {
    private Context context;
    private float rendimientoPractico;
    private Mash mash;
    private int layout;
    private boolean planned;

    public GrainListAdapter(Context context, Mash mash, boolean planned, int layout, float rendimientoPractico) {
        this.context = context;
        this.mash = mash;
        this.layout = layout;
        this.planned = planned;
        this.rendimientoPractico = rendimientoPractico;
    }

    @Override
    public int getCount() {
        return this.mash.getGrains().size();
    }

    @Override
    public Grain getItem(int position) {
        return mash.getGrains().get(position);
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

        String detail;
        if( planned){
            detail = "";
            // Tengo que saber si tengo la cantidad de experiencias alcanza para saber
            // el rendimiento practico-
            if(rendimientoPractico == 0.7)
            detail = mash.getGrains().get(position)
                    .getStringPlanned(mash.getDensidadObjetivo(), mash.getVolumen(), 0.7f);
            else
                detail = mash.getGrains().get(position)
                        .getStringPlanned(mash.getDensidadObjetivo(), mash.getVolumen(), 0.7f, rendimientoPractico);
        } else{
            detail = mash.getGrains().get(position)
                    .getStringPlanning();
        }

        grainDetail.setText(detail);

        return convertView;
    }
}
