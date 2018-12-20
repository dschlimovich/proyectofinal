package com.example.maceradores.maceracion.Fragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.adapters.StageFragmentAdapter;
import com.example.maceradores.maceracion.db.DatabaseHelper;
import com.example.maceradores.maceracion.models.Grain;
import com.example.maceradores.maceracion.models.Mash;
import com.example.maceradores.maceracion.models.MeasureInterval;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StageFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private int idMash;
    private int idExp;


    public StageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stage, container, false);
        final int idMash = getArguments().getInt("idMash"); //Me traigo el idMash q viene del viewpager
        final int idExp = getArguments().getInt("idExp");

        this.idMash=idMash;
        this.idExp=idExp;

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewStage);
        //List<MeasureInterval> intervals = getIntervals(idMash);
        //adapter = new StageFragmentAdapter(intervals, R.layout.item_list_stage);
        Mash mash = getMash(idMash);
        adapter = new StageFragmentAdapter(mash, R.layout.item_list_stage);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    private Mash getMash(int idMash){
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        Mash mash = dbHelper.getMash(idMash);
        mash.setGrains( dbHelper.getGrains(idMash));
        mash.setPlan(dbHelper.getMeasureIntervals(idMash));

        dbHelper.close();
        return mash;
    }
}
