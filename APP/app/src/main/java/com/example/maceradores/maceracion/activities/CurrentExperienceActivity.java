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
import android.widget.Toast;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.RetrofitGsonContainer.SensedValuesContainer;
import com.example.maceradores.maceracion.adapters.ViewPagerAdapter;
import com.example.maceradores.maceracion.db.DatabaseHelper;
import com.example.maceradores.maceracion.models.Experiment;
import com.example.maceradores.maceracion.models.Mash;
import com.example.maceradores.maceracion.models.SensedValues;

import java.util.Date;

public class CurrentExperienceActivity extends AppCompatActivity{
    //Data
    private Mash currentMash;
    private long idExp;
    private SensedValues currenteSensedValue;

    //UI
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    // LifeCycle functions.
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setear el toolbar
        setContentView(R.layout.activity_current_experience);
        setToolbar();
        setTabLayout();
        setViewPager();
        setListenerTabLayout(viewPager);
        //los fragments estan vacios porque aun no recibi ningun sensed values.

        // Saber de que maceración vine
        Intent intent = getIntent();
        if( intent.hasExtra("idMash") && intent.hasExtra("nameMash")){
            int idMash = intent.getIntExtra("idMash", 0);
            String nameMash = intent.getStringExtra("nameMash");
            setTitle("Medición " + nameMash);
            currentMash = loadCurrentMash(idMash);

            long newExperimentId = insertNewExperiment(idMash);
            if(newExperimentId == -1) {
                Toast.makeText(this, "Error al insertar experiencia", Toast.LENGTH_SHORT).show();
                // Me vuelvo al activity que lista las experiencias
                Intent intentError = new Intent(CurrentExperienceActivity.this, ExperimentActivity.class);
                intentError.putExtra("idMash", idMash);
                intentError.putExtra("nameMash", nameMash);
                startActivity(intentError);
            } else {
                //me guardo el id del experimento para obtener los sensed values despues.
                this.idExp = newExperimentId;
                // que mas necesito para empezar a medir:
                // -Los intervalos de medicion (tal vez con el de temperatura solo alcanza.
                // -Saber cuantas mediciones se van a realizar.
                // -Almacenar un Sensed Value con lo último obtenido(ojo que pH puede venir vacio.)
                //
                // y despues debería llamar un async task para que vaya midiendo sin que se me congele la cosa.

            } //end if insert new experiment.
        } else {
            Toast.makeText(this, "Usted ha llegado aqui de una manera misteriosa", Toast.LENGTH_SHORT).show();
        } //end if preguntando por los extras del intent.
    } //end onCreate

    // BD Functions.
    private long insertNewExperiment(int idMash) {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues experimentValues = new ContentValues();
        experimentValues.put("maceracion", idMash);
        long newExperimentId = db.insert("Experimento", null, experimentValues);
        dbHelper.close();
        return newExperimentId;
    }

    private Mash loadCurrentMash(int idMash){
        // De la maceracion actual necesito:
        //  - ID,   Nombre, Tipo, Intervalos de mecidicion, Densidad Obtjetivo.

        Mash mash = new Mash();

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(idMash)};

        Cursor cursor = db.query("Maceracion", null, selection, selectionArgs, null, null, null);

        if( cursor.moveToFirst()){
            //ID
            mash.setId( cursor.getInt( cursor.getColumnIndexOrThrow("id")));
            // Nombre
            mash.setName( cursor.getString( cursor.getColumnIndexOrThrow("nombre")));
            //Tipo
            mash.setTipo( cursor.getString( cursor.getColumnIndexOrThrow("tipo")));
            //Volumen
            mash.setVolumen( cursor.getFloat( cursor.getColumnIndexOrThrow("volumen")));
            // Densidad Objtetivo
            mash.setDensidadObjetivo(cursor.getFloat(cursor.getColumnIndexOrThrow("densidadObjetivo")));
            //Intervalo de Medicion de temperatura.
            mash.setPeriodMeasureTemperature(cursor.getInt( cursor.getColumnIndexOrThrow("intervaloMedTemp")));
            // Intervalo de Medicion de PH
            mash.setPeriodMeasurePh( cursor.getInt( cursor.getColumnIndexOrThrow("intervaloMedPh")));
        }  //Seguramente hay valores que no voy a utilizar.

        cursor.close();
        db.close();
        return mash;
    }

    private long insertSensedValue(SensedValuesContainer svc){
        // creo la instancia de basede datos para insertar.
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put("id_exp", idExp); //ESTO O SE HARDCODEA O SE OBTIENE DE OTRO LADO.
        values.put("id", svc.getId());
        values.put("fechayhora", svc.getFechayhora());
        values.put("temp1", svc.getTemp1());
        values.put("temp2", svc.getTemp2());
        values.put("temp3", svc.getTemp3());
        values.put("temp4", svc.getTemp4());
        values.put("temp5", svc.getTemp5());
        values.put("tempPh", svc.getTempPh());
        values.put("tempAmb", svc.getTempAmb());
        values.put("pH", svc.getpH());

        long newSensedValueId = db.insert("SensedValues", null, values);
        dbHelper.close();
        return newSensedValueId; //si devuelve -1 es porque no pudo insertar
    }

    // ------ Toolbar Functions----------
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
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),this,tabLayout.getTabCount());
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





}
