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

public class InputCalculationAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private float rendimientoEquipo;
    private Mash mash;
    private int typeCalc;

    // static attributes
    public static final int TEORICO = 1;
    public static final int AJUSTADO = 2;
    public static final int PRACTICO = 3;


    public InputCalculationAdapter(Context context, Mash mash, int layout, float rendimientoEquipo, int typeCalc) {
        this.context = context;
        this.mash = mash;
        this.layout = layout;
        this.rendimientoEquipo = rendimientoEquipo;
        this.typeCalc = typeCalc;
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
        float volumen = mash.getVolumen();
        float densidad = mash.getDensidadObjetivo();

        switch( this.typeCalc){
            case TEORICO:
                detail = mash.getGrains().get(position).getName() +
                        " Cantidad: " + mash.getGrains().get(position).getMaltTheoritical( densidad, volumen, 0.7f);
                break;
            case AJUSTADO:
                detail = mash.getGrains().get(position).getName() +
                        " Cantidad: " + mash.getGrains().get(position).getMaltTheoritical( densidad, volumen, rendimientoEquipo);
                break;
            case PRACTICO:
                detail = mash.getGrains().get(position).getName() +
                        " Cantidad: " + mash.getGrains().get(position).getMaltPractical( densidad, volumen, rendimientoEquipo);
                break;
            default:
                break;
        }


        calcInputs.setText(detail);

        return convertView;
    }

}
