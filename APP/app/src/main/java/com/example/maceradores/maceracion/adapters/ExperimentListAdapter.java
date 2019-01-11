package com.example.maceradores.maceracion.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.models.Experiment;

import java.util.List;

public class ExperimentListAdapter extends RecyclerView.Adapter<ExperimentListAdapter.ViewHolder> {
    private List<Experiment> experimentList;
    private int layout;
    private ExperimentListAdapter.onItemClickListener listener;

    public ExperimentListAdapter(List<Experiment> experimentList, int layout, onItemClickListener listener) {
        this.experimentList = experimentList;
        this.layout = layout;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(this.layout, viewGroup, false);
        return new ExperimentListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(this.experimentList.get(i), this.listener);
    }

    @Override
    public int getItemCount() {
        return this.experimentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameMash;
        // TODO change the id to one more general.

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvNameMash = (TextView) itemView.findViewById(R.id.tvItemListMash);
        }

        public void bind(final Experiment experiment, final onItemClickListener listener) {
            tvNameMash.setText(experiment.getDate().toString());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickItem(experiment, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClickItem(experiment, getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public interface onItemClickListener{
        void onClickItem(Experiment experiment, int position);
        void onLongClickItem(Experiment experiment, int position);
    }
}
