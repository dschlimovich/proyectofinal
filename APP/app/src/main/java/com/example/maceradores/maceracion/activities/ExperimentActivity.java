package com.example.maceradores.maceracion.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.adapters.ExperimentListAdapter;
import com.example.maceradores.maceracion.db.DatabaseHelper;
import com.example.maceradores.maceracion.models.Experiment;

import java.util.ArrayList;
import java.util.List;

public class ExperimentActivity extends AppCompatActivity {
    // Data
    private int idMash;
    private String nameMash;
    private List<Experiment> experimentList;


    //UI
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FloatingActionButton fab;

    // LifeCycle functions
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if(intent.hasExtra("idMash")){
            this.idMash = intent.getIntExtra("idMash", -1);
            setNameMashTitle(idMash);
        }
        else{
            startActivity(new Intent(ExperimentActivity.this, MainActivity.class));
            finish();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMash);

        fab = (FloatingActionButton) findViewById(R.id.fabCurrentValues);
        fab.setImageResource(android.R.drawable.ic_media_play);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ExperimentActivity.this, CurrentExperienceActivity.class);
                intent.putExtra("idMash", idMash);
                startActivity(intent);
            }
        });
        setToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        experimentList = getAllExperiments(this.idMash);
        //experimentList = new ArrayList<Experiment>(){{add(new Experiment(1,null));}};

        rvAdapter = new ExperimentListAdapter(experimentList, R.layout.item_list_mash, new ExperimentListAdapter.onItemClickListener() {
            @Override
            public void onLongClickItem(Experiment experiment, int position) {
                experimentList.remove(position);
                rvAdapter.notifyItemRemoved(position);
                // lo tengo que eliminar de la base de datos tambien.
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                int cant = dbHelper.deleteExperiment(experiment.getId());

                if(cant == 1)
                    Toast.makeText(ExperimentActivity.this, "Experimento eliminado", Toast.LENGTH_SHORT).show();

                dbHelper.close();

            }
        });
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_experiments_activity, menu);
        return super.onCreateOptionsMenu(menu);
    } //end onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.deleteMash:
                    deleteMash();
                return true;
            case R.id.seePlanification:
                // we should to show the planning activity with the values charged.
                // but if the are one or more experiments done, i shouldn't change the plannification.
                Intent intent = new Intent(ExperimentActivity.this, PlanningActivity.class);
                intent.putExtra("idMash", idMash);
                startActivity(intent);
                return true;
            case R.id.staticsAllExperiments:
                if(experimentList.isEmpty()){
                    Toast.makeText(this, "No existen Experimentos de Maceración para mostrar", Toast.LENGTH_LONG).show();
                    return  true;
                }else{
                    Intent intent2 = new Intent(ExperimentActivity.this, MashExpHistoryActivity.class);
                    intent2.putExtra("idMash", idMash);
                    startActivity(intent2);
                }

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    // User Interface functions
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)//Esto es para que me deje usar el Toolbar q empieza e la APU 24
    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_MainActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().show();
    }

    // DB functions
    private List<Experiment> getAllExperiments(int idMash) {
        List<Experiment> resultados = new ArrayList<Experiment>();
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        resultados = dbHelper.getAllExperiments(idMash);
        dbHelper.close();
        return resultados;
    } //end getAllExperiments

    private void deleteMash() {
        //tengo que eliminar todos los datos y tengo que mandar al main activity.
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        int cant = dbHelper.deleteMash(this.idMash);
        if (cant == 1)
            Toast.makeText(this, "Maceración eliminada", Toast.LENGTH_SHORT).show();

        dbHelper.close();

        startActivity(new Intent(ExperimentActivity.this, MainActivity.class));
        finish();
    }

    private void setNameMashTitle(int idMash){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        String name = dbHelper.getNameMash(idMash);
        setTitle(name);
        dbHelper.close();
    }


}//end ExperimentActivity
