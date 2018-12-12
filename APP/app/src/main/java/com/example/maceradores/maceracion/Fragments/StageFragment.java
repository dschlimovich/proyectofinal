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
        final int idMash = getArguments().getInt("idMash"); //Me traigo el idMash q viene del viewpager
        final int idExp = getArguments().getInt("idExp");
        this.idMash=idMash;


        this.idExp=idExp;
        return inflater.inflate(R.layout.fragment_stage, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerViewStage);
        List<MeasureInterval> intervals = getIntervals(idMash);
        adapter = new StageFragmentAdapter(intervals, R.layout.item_list_stage);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private List<MeasureInterval> getIntervals(int idMash){
        List<MeasureInterval> intervals = new ArrayList<MeasureInterval>();
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //String[] columns = {"idRaspi"};
        String selection = "maceracion = ?";
        String[] selectionArgs = { String.valueOf(idMash)};
        Log.d("StageFragment", "el id de maceracion es: " + idMash);

        Cursor cursor = db.query("Intervalo", null, selection, selectionArgs, null, null, null);
        while(cursor.moveToNext()){
            int order = cursor.getInt(cursor.getColumnIndexOrThrow("orden"));
            int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duracion"));

            float temperature = cursor.getFloat(cursor.getColumnIndexOrThrow("temperatura"));
            float temperatureDeviation = cursor.getFloat(cursor.getColumnIndexOrThrow("desvioTemperatura"));

            float ph = cursor.getFloat(cursor.getColumnIndexOrThrow("ph"));
            float phDeviation = cursor.getFloat(cursor.getColumnIndexOrThrow("desvioPh"));

            float temperatureDecoccion = cursor.getFloat(cursor.getColumnIndexOrThrow("tempDecoccion"));
            float temperatureDecoccionDeviation = cursor.getFloat(cursor.getColumnIndexOrThrow("desvioTempDecoccion"));

            //con estos tres valores puedo crear el intervalo y agregarlo.
            MeasureInterval interval = new MeasureInterval(order, temperature, temperatureDeviation, temperatureDecoccion, temperatureDecoccionDeviation, ph, phDeviation, duration);
            intervals.add(interval);

        }
        cursor.close();
        db.close();

        Log.d("StageFragment", "tamaño de intervalos: " + intervals.size());
        return intervals;
    }
}
