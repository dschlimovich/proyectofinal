package com.example.maceradores.maceracion.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.models.MeasureInterval;

import org.w3c.dom.Text;

import java.util.List;

public class StageFragmentAdapter extends RecyclerView.Adapter<StageFragmentAdapter.ViewHolder> {
    private int stageInProgress;
    private List<MeasureInterval> intervals;
    private int layout;
    //necesito Listener para el click?


    public StageFragmentAdapter(List<MeasureInterval> intervals, int layout) {
        this.intervals = intervals;
        this.layout = layout;
        stageInProgress = 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(this.layout, viewGroup, false);
        return new StageFragmentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(this.intervals.get(i));
    }

    @Override
    public int getItemCount() {
        return intervals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView numberStage;
        private Chronometer durationStage;
        private Chronometer remainingStage;
        private TextView temperatureStage;
        private TextView phStage;
        private TextView temperatureDecoccionStage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.numberStage = (TextView) itemView.findViewById(R.id.textViewNumberStage);
            this.durationStage = (Chronometer) itemView.findViewById(R.id.chronometerDurationStage);
            this.remainingStage = (Chronometer) itemView.findViewById(R.id.chronometerRemainingStage);
            this.phStage = (TextView) itemView.findViewById(R.id.textViewPhStage);
            this.temperatureDecoccionStage = (TextView) itemView.findViewById(R.id.textViewTemperatureDecoccionStage);

        }

        public void bind(MeasureInterval interval) {
            //the interval have an order, no?
            String stage = String.valueOf(interval.getOrder());
            numberStage.append(stage);
        }
    }
}
