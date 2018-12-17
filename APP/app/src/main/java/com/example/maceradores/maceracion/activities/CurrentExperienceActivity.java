package com.example.maceradores.maceracion.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.WorkManager.MyWorker;
import com.example.maceradores.maceracion.adapters.ViewPagerAdapterCurrent;
import com.example.maceradores.maceracion.db.DatabaseHelper;
import com.example.maceradores.maceracion.retrofitInterface.Api;
import com.google.gson.JsonObject;

import java.util.UUID;
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
    private ViewPagerAdapterCurrent adapter;
    private UUID workId;

    // LifeCycle functions.
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if( intent.hasExtra("idMash") ){
            idMash = intent.getIntExtra("idMash", 0);
            setTitleName(idMash);
            //setTitle("Medición " + intent.getStringExtra("nameMash"));
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

                this.workId = simpleRequest.getId();//Guardo el id del workid


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

    private void setTitleName(int idMash) {
        // saber el periodo de medicion.
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {"nombre"};
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(idMash)};

        Cursor cursor = db.query("Maceracion", columns, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()){
            String name = cursor.getString(0); //como tengo una sola columna, devuelvo la primera nomas.
            setTitle("Maceración" + name);
        }
        cursor.close();
        db.close();
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
                cancelExperiment(this.idExperiment,this.idMash);
                return true;
            case R.id.acceptCurrentExperience:
                acceptExperiment(this.idExperiment, this.idMash);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void acceptExperiment(int idExperiment, int idMash) {
        // primero debería saber si ya se ejecutaron todas las mediciones planificadas.
        int medicionesRealizadas = amountSensedValue(idExperiment);
        int cadaCuantoMido = intervaloMedicion(idMash);
        int medicionesARealizar = cantMediciones( idMash, cadaCuantoMido);
        Log.d("Mediciones a realizar: ",String.valueOf(medicionesARealizar/2));
        Log.d("Mediciones realizadas: ",String.valueOf(medicionesRealizadas));


        if( medicionesRealizadas == medicionesARealizar/2){
            // Mostrar el alertDialog para finalizar la experiencia.
            // Tiene que insertar la densidad obtenida en el experimento.
            showAlertFinishExperience();
        } else {
            Toast.makeText(this, "Aun no se realizaron todas las mediciones correspondientes", Toast.LENGTH_SHORT).show();
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
        adapter = new ViewPagerAdapterCurrent(getSupportFragmentManager(),this,tabLayout.getTabCount(),idMash, idExperiment);
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

    private void cancelExperiment(int idExp, int idMash){
    //---Cancel Worker
        WorkManager.getInstance().cancelWorkById(this.workId);//Cancelo el Worker

    //---Move to the Experiment Activity
        Intent intent = new Intent(CurrentExperienceActivity.this, ExperimentActivity.class);
        intent.putExtra("idMash", idMash);
        startActivity(intent);

    //---Cancel FragmentManager
//        getSupportFragmentManager().beginTransaction().
//                remove(getSupportFragmentManager().findFragmentById(R.id.fragmentMeasure)).commit();

    //---Elimino de la DB de la APP LOCAL
        deleteExperiment(idExp);

    //---Llamo a la API para eliminar del Raspi
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

    private void deleteExperiment( int idExp){
        // elemino todos los sensed values del experimentos
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "id = ?";
        String [] selectionArgs = new String[] { String.valueOf(idExp)};

        db.delete("SensedValues", "id_exp = ?", selectionArgs);
        int cant = db.delete("Experimento", selection, selectionArgs);
        if(cant == 1)
            Toast.makeText(CurrentExperienceActivity.this, "Experimento eliminado", Toast.LENGTH_SHORT).show();

        dbHelper.close();
    }

    public int amountSensedValue(int idExp){
        //int amount = 0;
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int amount = (int) DatabaseUtils.queryNumEntries(db, "SensedValues", "id_exp=?", new String[] {String.valueOf(idExp)});
        db.close();
        return amount;
    }

    private int cantMediciones( int idMash, int intervaloMedicion){
        //TODO hacer con medicionesPorIntervalo

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(duracion) FROM Intervalo WHERE maceracion = ?", new String[]{String.valueOf(idMash)});
        int duracionTotal = 0;
        if(c.moveToFirst()){
            duracionTotal = c.getInt(0);
        }
        c.close();
        db.close();
        return (duracionTotal * 60) / (intervaloMedicion/2);
    }

    private int intervaloMedicion (int idMash){
        int intervaloMedicion = 0;
        // saber el periodo de medicion.
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {"intervaloMedTemp"};
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(idMash)};

        Cursor cursor = db.query("Maceracion", columns, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()){
            intervaloMedicion = cursor.getInt(0); //como tengo una sola columna, devuelvo la primera nomas.
        }
        cursor.close();
        db.close();
        if(intervaloMedicion<30)intervaloMedicion=30; // Minimo Intervalo de Medicion es de 30 seg

        return intervaloMedicion;
    }

    private void showAlertFinishExperience(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Finalizar Experimento");

        View finishExperienceView = LayoutInflater.from(this).inflate(R.layout.dialog_finish_experience, null);
        builder.setView(finishExperienceView);
        //Ahora tenemos que obtener las referencia y cargarla
        final EditText editTextDensity = (EditText) finishExperienceView.findViewById(R.id.editTextFinishExperienceDensity);
        //agrego boton para cerrar el dialogo

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //necesito insertarle la densidad al experimento.
                float density = Float.valueOf(editTextDensity.getText().toString());
                insertDensity(idExperiment, density);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();

    }

    private void insertDensity( int idExperiment, float density){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues experimentValues = new ContentValues();
        experimentValues.put("densidad", density );
        String whereClausule = "id = ?";
        String[] whereClausuleArgs = new String[] {String.valueOf(idExperiment)};
        int cant = db.update("Experimento", experimentValues,whereClausule, whereClausuleArgs);
        dbHelper.close();
        if(cant == 1)
            Toast.makeText(this, "Valor de densidad insertado correctamente", Toast.LENGTH_SHORT).show();
    }


}
