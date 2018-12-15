package com.example.maceradores.maceracion.activities;

import android.content.ContentValues;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
        }



        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMash);

        fab = (FloatingActionButton) findViewById(R.id.fabCurrentValues);
        fab.setImageResource(android.R.drawable.ic_media_play);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creo un nuevo experimento
                // y llamo al activity de medicion con el id
                // de esta ultima insercion
                // TODO definir quien crea el experimento. El currentExperienceActivity o Experiment Activity


                Intent intent = new Intent(ExperimentActivity.this, CurrentExperienceActivity.class);
                intent.putExtra("idMash", idMash);
                intent.putExtra("nameMash", nameMash);
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
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String selection = "id = ?";
                String [] selectionArgs = new String[] { String.valueOf(experiment.getId())};

                db.delete("SensedValues", "id_exp = ?", new String[] {String.valueOf(experiment.getId())});
                int cant = db.delete("Experimento", selection, selectionArgs);
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
                // we should to show the planning activity with the values chargued.
                // but if the are one or more experiments done, i shouldn't change the plannification.
                Intent intent = new Intent(ExperimentActivity.this, PlanningActivity.class);
                intent.putExtra("idMash", idMash);
                startActivity(intent);
                return true;
            case R.id.staticsAllExperiments:
                Intent intent2 = new Intent(ExperimentActivity.this, PlanningActivity.class);
                intent2.putExtra("idMash", idMash);
                startActivity(intent2);

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
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Filter results WHERE "title" = 'My Title'
        //String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(this.idMash)};

        Cursor cursor = db.rawQuery("SELECT E.id AS 'id', strftime('%d/%m/%Y %H:%M', E.fecha) AS 'fecha' FROM Experimento AS E INNER JOIN Maceracion AS M ON E.maceracion = M.id WHERE M.id = ? ORDER BY E.fecha DESC", selectionArgs);

        //List itemNames = new ArrayList<>();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
            );
            String fecha = cursor.getString(
                    cursor.getColumnIndexOrThrow("fecha")
            );

            resultados.add(new Experiment(id, fecha));
        } // end while
        cursor.close();
        dbHelper.close();
        return resultados;
    } //end getAllExperiments

    private void deleteMash() {
        //tengo que eliminar todos los datos y tengo que mandar al main activity.
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //elimino primero todos los experimentos. Despues la maceracion

        String selection = "maceracion = ?";
        String [] selectionArgs = new String[] { String.valueOf(this.idMash)};

        db.delete("Experimento", selection, selectionArgs);

        // Quito los granos
        db.delete("Grano", selection, selectionArgs);

        //Quito los intervalos de medicion
        db.delete("Intervalo", selection, selectionArgs);

        //Ahora elimino la maceracion y vuelvo al main activity
        selection = "id = ?";
        selectionArgs = new String[] { String.valueOf(this.idMash)};
        int cant = db.delete("Maceracion", selection, selectionArgs );
        if (cant == 1)
        Toast.makeText(this, "Maceración eliminada", Toast.LENGTH_SHORT).show();

        dbHelper.close();

        startActivity(new Intent(ExperimentActivity.this, MainActivity.class));
    }

    private void setNameMashTitle(int idMash){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //elimino primero todos los experimentos. Despues la maceracion

        String[] columns = new String[]{"nombre"};
        String selection = "id = ?";
        String [] selectionArgs = new String[] { String.valueOf(this.idMash)};

        Cursor c = db.query("Maceracion", columns, selection, selectionArgs, null, null, null);
        if(c.moveToFirst()){
            String name = c.getString(0);
            setTitle(name);
        }
        c.close();
        dbHelper.close();
    }


}//end ExperimentActivity
