package com.example.maceradores.maceracion.adapters;

import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.maceradores.maceracion.Fragments.MeasureFragment;
import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.models.Mash;
import com.example.maceradores.maceracion.models.MeasureInterval;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class StageFragmentAdapter extends RecyclerView.Adapter<StageFragmentAdapter.ViewHolder> {
    //private List<MeasureInterval> intervals;
    private int layout;
    private Mash mash;


    public StageFragmentAdapter(Mash mash, int layout) { //List<MeasureInterval> intervals
        //this.intervals = intervals;
        //Log.d("StageFragmentAdapter", "tamaño de intervalos : " + intervals.size());
        this.mash = mash;
        this.layout = layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(this.layout, viewGroup, false);
        return new StageFragmentAdapter.ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(this.mash, i);
    }

    @Override
    public int getItemCount() {
        return mash.getPlan().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView numberStage;
        private Chronometer durationStage;
        private Chronometer remainingStage;
        private TextView temperatureStage;
        private TextView phStage;
        private TextView temperatureDecoccionStage;
        private TextView planningStage;
        private LinearLayout linearLayoutRemainingStage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.numberStage = (TextView) itemView.findViewById(R.id.textViewNumberStage);
            this.durationStage = (Chronometer) itemView.findViewById(R.id.chronometerDurationStage);
            this.remainingStage = (Chronometer) itemView.findViewById(R.id.chronometerRemainingStage);
            this.temperatureStage = (TextView) itemView.findViewById(R.id.textViewTemperatureStage);
            this.phStage = (TextView) itemView.findViewById(R.id.textViewPhStage);
            this.temperatureDecoccionStage = (TextView) itemView.findViewById(R.id.textViewTemperatureDecoccionStage);
            this.planningStage = (TextView) itemView.findViewById(R.id.textViewPlanningStage);
            this.linearLayoutRemainingStage = (LinearLayout) itemView.findViewById(R.id.linearLayoutRemainingStage);


        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void bind(Mash mash, int position) {
            //the interval have an order, no?
            MeasureInterval interval = mash.getPlan().get(position);
            String stage = String.valueOf(interval.getOrder());
            numberStage.append(stage);
            //la duracion de la etapa esta expresada en minutos.
            int duration = interval.getDuration();
            durationStage.setBase( SystemClock.elapsedRealtime() - duration*60000);

            // tiempo restante.
            if( interval.getOrder() == 1){
                linearLayoutRemainingStage.setVisibility(View.INVISIBLE);
            } else {
                int remaining = 0;
                for( int i = 0; i < interval.getOrder() - 1; i++ ){
                    remaining = remaining + mash.getPlan().get(i).getDuration();
                }
                remainingStage.setBase( SystemClock.elapsedRealtime() + remaining*60000);
                remainingStage.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        if( "00:00".contentEquals(chronometer.getText())){
                            chronometer.stop();
                            linearLayoutRemainingStage.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                remainingStage.setCountDown(true);
                remainingStage.start();

            }

            //temperatura + desviacion-
            StringBuilder builder = new StringBuilder();
            builder.append(interval.getMainTemperature());
            builder.append(" ± ");
            builder.append( interval.getMainTemperatureDeviation() ) ;
            builder.append( " °C ");
            temperatureStage.append( builder.toString());

            //ph + desviacion
            phStage.append( interval.getpH() + " ± " + interval.getPhDeviation());

            //temperatura decoccion
            if(interval.getSecondTemperature() == -1000)
                temperatureDecoccionStage.setText("");
            else
                temperatureDecoccionStage.append( interval.getSecondTemperature() + " ± " + interval.getSecondTemperatureDeviation() + " °C " );

            planningStage.setText( mash.getPlanning(position));
        }
    }
}
