package com.example.maceradores.maceracion.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.maceradores.maceracion.Fragments.MeasureFragment;
import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.WorkManager.MyWorker;
import com.example.maceradores.maceracion.adapters.ViewPagerAdapter;
import com.example.maceradores.maceracion.db.DatabaseHelper;
import com.example.maceradores.maceracion.retrofitInterface.Api;
import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CurrentExperienceActivity extends AppCompatActivity{
    //Data
    private int idMash;
    private int idExperiment;

    //UI
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    // LifeCycle functions.
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if( intent.hasExtra("idMash") && intent.hasExtra("nameMash")){
            idMash = intent.getIntExtra("idMash", 0);
            setTitle("Medición " + intent.getStringExtra("nameMash"));
            long newExperimentId = insertNewExperiment(idMash);
            if(newExperimentId == -1) {
                Toast.makeText(this, "Error al insertar experiencia", Toast.LENGTH_SHORT).show();
                // TODO volver al activity
            } else{
                // pudo insertar
                this.idExperiment = (int) newExperimentId;
                sendNewExperiment(idExperiment); //ESTA ES LA LLAMADA A LA API PARA EMPEZAR LA EXP
                //--------------WorkManager---------------------
                Data data = new Data.Builder()
                        .putString(MyWorker.IDEXP, String.valueOf(idExperiment))
                        .build();

                final OneTimeWorkRequest simpleRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                        .setInputData(data)
                        .build();
                WorkManager.getInstance().enqueue(simpleRequest);

                setContentView(R.layout.activity_current_experience);
                setToolbar();
                setTabLayout();
                setViewPager();
                setListenerTabLayout(viewPager);
            }
        } else {
            Toast.makeText(this, "Usted ha llegado aqui de una manera misteriosa", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_current_experience, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cancelCurrentExperience:
                cancelExperiment(this.idExperiment);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private long insertNewExperiment(int idMash) {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues experimentValues = new ContentValues();
        experimentValues.put("maceracion", idMash);
        long newExperimentId = db.insert("Experimento", null, experimentValues);
        dbHelper.close();
        return newExperimentId;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)//Esto es para que me deje usar el Toolbar q empieza e la APU 24
    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().show();
    }

    private void setTabLayout(){
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("MEDICIONES"));//Esta tiene la posicion 0
        tabLayout.addTab(tabLayout.newTab().setText("ETAPAS"));
    }

    private void setViewPager(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),this,tabLayout.getTabCount(),idMash, idExperiment);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void setListenerTabLayout (final ViewPager viewPager){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void cancelExperiment(int idExp){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(240, TimeUnit.SECONDS)
                .writeTimeout(240, TimeUnit.SECONDS)
                .build();

        //Luego lo agrego a la llamada de Retrofit

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        //------Build new JsonObject with Experiment to be send
        //{ "nombre": "pepito","idExp":"1", "duracion_min": "1","intervaloMedicionTemp_seg":"15","intervaloMedicionPH_seg":"15" }
        JsonObject cancelExp= new JsonObject();
        cancelExp.addProperty("idExp",Integer.toString(idExp));

        Api api = retrofit.create(Api.class);
        Call<Void> call = api.cancelExperiment(cancelExp);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Toast.makeText(getApplicationContext(),"Experimento de Maceración Cancelado",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        //finish();
    }

    private void sendNewExperiment(int newExperimentId) {
        //necesito nombre maceracion, id Experimento, duraricion total, intervalo medicion temperatura, intervalo medicion ph
        String nameMash = "";
        int duracionTotal = 0;
        int intervaloMedicionTemp = 0;
        int intervaloMedicionPh = 0;

        // saber el periodo de medicion.
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {"nombre", "intervaloMedTemp", "intervaloMedPh"};
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(this.idMash)};

        Cursor cursor = db.query("Maceracion", columns, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()){
            nameMash = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            intervaloMedicionTemp = cursor.getInt(cursor.getColumnIndexOrThrow("intervaloMedTemp"));
            intervaloMedicionPh = cursor.getInt(cursor.getColumnIndexOrThrow("intervaloMedPh"));
        }
        cursor.close();
        db.close();
        if(intervaloMedicionTemp<30)intervaloMedicionTemp=30; // Minimo Intervalo de Medicion es de 30 seg

        dbHelper = new DatabaseHelper(getApplicationContext());
        db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(duracion) FROM Intervalo WHERE maceracion = ?", new String[]{String.valueOf(idMash)});
        if(c.moveToFirst()){
            duracionTotal = c.getInt(0);
        }
        c.close();
        db.close();

        executeNewMashExperiment(nameMash, newExperimentId, duracionTotal, intervaloMedicionTemp, intervaloMedicionPh);
    }

    private void executeNewMashExperiment(String nombre, int idExp, int duracion_min, int intervaloMedicionTemp_seg,int intervaloMedicionPH_seg){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(240, TimeUnit.SECONDS)
                .writeTimeout(240, TimeUnit.SECONDS)
                .build();

        //Luego lo agrego a la llamada de Retrofit

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        //------Build new JsonObject with Experiment to be send
        //{ "nombre": "pepito","idExp":"1", "duracion_min": "1","intervaloMedicionTemp_seg":"15","intervaloMedicionPH_seg":"15" }
        JsonObject NewExperiment= new JsonObject();
        NewExperiment.addProperty("nombre",nombre);
        NewExperiment.addProperty("idExp",Integer.toString(idExp));
        NewExperiment.addProperty("duracion_min",Integer.toString(duracion_min));
        NewExperiment.addProperty("intervaloMedicionTemp_seg",Integer.toString(intervaloMedicionTemp_seg));
        NewExperiment.addProperty("intervaloMedicionPH_seg",Integer.toString(intervaloMedicionPH_seg));

        Api api = retrofit.create(Api.class);
        Call<Void> call = api.postExperiment(NewExperiment);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });



    }




}
