package com.example.maceradores.maceracion.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ExpandableListAdapter;
import android.widget.Toast;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.adapters.ExperimentListAdapter;
import com.example.maceradores.maceracion.db.DatabaseHelper;
import com.example.maceradores.maceracion.models.Experiment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExperimentActivity extends AppCompatActivity {
    // Data
    private int idMash;
    private List<Experiment> experimentList;

    //UI
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if(intent.hasExtra("idMash")){
            //Toast.makeText(ExperimentActivity.this, "" + intent.getIntExtra("idMash", -1), Toast.LENGTH_SHORT).show();
            this.idMash = intent.getIntExtra("idMash", -1);
            //insertExperiment(idMash);
        }
        else{
            startActivity(new Intent(ExperimentActivity.this, MainActivity.class));
        }
        if(intent.hasExtra("nameMash")){
            setTitle(intent.getStringExtra("nameMash"));
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMash);

        fab = (FloatingActionButton) findViewById(R.id.fabCurrentValues);
        fab.setImageResource(android.R.drawable.ic_media_play);
    }

    private void insertExperiment(int idMash) {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Ahora puedo escribir en la base de datos,
        ContentValues values = new ContentValues();
        values.put("densidad", 1); //el nombre tiene la clausula unique
        values.put( "maceracion", 1);
        long newRowId = db.insert("Experimento", null, values);
        Toast.makeText(this, "Inserto un valor", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        experimentList = getAllExperiments(this.idMash);
        //experimentList = new ArrayList<Experiment>(){{add(new Experiment(1,null));}};

        rvAdapter = new ExperimentListAdapter(experimentList, R.layout.item_list_mash, new ExperimentListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Experiment experiment, int position) {

            }
        });
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private List<Experiment> getAllExperiments(int idMash) {
        List<Experiment> resultados = new ArrayList<Experiment>();
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Filter results WHERE "title" = 'My Title'
        //String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(this.idMash)};

        Cursor cursor = db.rawQuery("SELECT E.id, E.fecha FROM Experimento AS E INNER JOIN Maceracion AS M ON E.maceracion = M.id WHERE M.id = ? ORDER BY E.fecha DESC", selectionArgs);

        //List itemNames = new ArrayList<>();
        while(cursor.moveToNext()) {
            //señor problemon que no me guarde las fechas.
            //Long prueba = cursor.getLong(cursor.getColumnIndexOrThrow("fecha")); //doesnt work-
            //int columnas = cursor.getColumnCount();
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
            );
            String fecha = cursor.getString(
                    cursor.getColumnIndexOrThrow("fecha")
            );

            resultados.add(new Experiment(id, fecha));

            //Toast.makeText(this, "Cara de puto", Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, fecha, Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        return resultados;
    }
}//end ExperimentActivity
