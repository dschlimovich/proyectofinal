package com.example.maceradores.maceracion.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.models.Mash;
import com.example.maceradores.maceracion.models.MeasureInterval;

import org.w3c.dom.Text;

import java.util.List;

public class IntervalListAdapter extends RecyclerView.Adapter<IntervalListAdapter.ViewHolder> {
    private Mash mash;
    private boolean planned;
    private int layout;
    private IntervalListAdapter.onItemClickListener listener;

    public IntervalListAdapter(Mash mash, boolean planned, int layout, onItemClickListener listener) {
        //this.intervals = intervals;
        this.mash = mash;
        this.layout = layout;
        this.listener = listener;
        this.planned = planned;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(this.layout, viewGroup, false);
        return new IntervalListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //viewHolder.bind(this.intervals.get(i), this.listener);
        if(planned) {
            // no puedo pasar el mash.
            viewHolder.bind(this.mash, i);
        } else {
            viewHolder.bind(this.mash.getPlan().get(i), this.listener);
        }
    }

    @Override
    public int getItemCount() {
        return this.mash.getPlan().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView stage;
        private TextView detail;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.stage = (TextView) itemView.findViewById(R.id.textViewIntervalOrder);
            this.detail = (TextView) itemView.findViewById(R.id.textViewIntervalDetail);

        }

        public void bind(final MeasureInterval interval, final onItemClickListener listener) {
            //Here i load the values of my model in the UI
            // and link the listener
            //tengo que setear todos los campos-
            this.stage.setText( "ETAPA " + (getAdapterPosition() + 1));
            this.detail.setText(interval.getDescription());

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemClick(interval, getAdapterPosition());
                    return false;
                }
            });

        }

        public void bind(Mash mash, int position) {
            // si cai aca es porque ya esta planificado y tengo que hacer algo distinto.
            //Ahora es cuando se arma la batuta muajajajaja.
            this.stage.setText( "ETAPA " + (getAdapterPosition() + 1));
            this.detail.setText(mash.getPlan().get(position).getDescription());
            this.detail.append(mash.getPlanning(position));
        }
    }

    public interface onItemClickListener{
        public void onItemClick(MeasureInterval interval, int position);
    }
}
