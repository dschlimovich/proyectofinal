package com.example.maceradores.maceracion.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.models.Mash;

import java.util.List;

public class MashListAdapter extends RecyclerView.Adapter<MashListAdapter.ViewHolder> {
    private List<Mash> mashList;
    private int layout;
    private MashListAdapter.onItemClickListener listener;

    public MashListAdapter(List<Mash> mashList, int layout, onItemClickListener listener) {
        this.mashList = mashList;
        this.layout = layout;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Here i have to link layout with viewholder
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(this.layout, viewGroup, false);
        return new MashListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(this.mashList.get(i), this.listener);
    }

    @Override
    public int getItemCount() {
        return this.mashList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //All ui elements of each item
        private TextView tvNameMash;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvNameMash = (TextView) itemView.findViewById(R.id.tvItemListMash);

        }

        public void bind(final Mash mash, final onItemClickListener listener) {
            //Here i load the values of my model in the UI
            // and link the listener
            tvNameMash.setText(mash.getName() + " \t\t\t\t\t\t\t\t\t " + mash.getTipo());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(mash, getAdapterPosition());
                }
            });

        }
    }

    public interface onItemClickListener{
        public void onItemClick(Mash mash, int position);
    }
}
