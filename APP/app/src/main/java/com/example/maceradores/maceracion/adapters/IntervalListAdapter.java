package com.example.maceradores.maceracion.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.models.MeasureInterval;

import java.util.List;

public class IntervalListAdapter extends RecyclerView.Adapter<IntervalListAdapter.ViewHolder> {
    List<MeasureInterval> intervals;
    int layout;
    IntervalListAdapter.onItemClickListener listener;

    public IntervalListAdapter(List<MeasureInterval> intervals, int layout, onItemClickListener listener) {
        this.intervals = intervals;
        this.layout = layout;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(this.layout, viewGroup, false);
        return new IntervalListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(this.intervals.get(i), this.listener);
    }

    @Override
    public int getItemCount() {
        return intervals.size();
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
            this.stage.setText( "INTERVALO " + (getAdapterPosition() + 1));
            this.detail.setText(interval.getDescription());

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemClick(interval, getAdapterPosition());
                    return false;
                }
            });
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(interval, getAdapterPosition());
                }
            });*/

        }
    }

    public interface onItemClickListener{
        public void onItemClick(MeasureInterval interval, int position);
    }
}
