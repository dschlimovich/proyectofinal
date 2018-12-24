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

public class InputCalculation extends BaseAdapter {
    private Context context;
    private int layout;
    private float rendimientoEquipo;
    private Mash mash;


    public InputCalculation(Context context, Mash mash, int layout, float rendimientoEquipo,int TypeCalc) {
        this.context = context;
        this.mash = mash;
        this.layout = layout;
        this.rendimientoEquipo = rendimientoEquipo;
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

        TextView calcInputs = (TextView) convertView.findViewById(R.id.textViewGrainDetail);

        String detail = new String();


        calcInputs.setText(detail);

        return convertView;
    }
}


}
