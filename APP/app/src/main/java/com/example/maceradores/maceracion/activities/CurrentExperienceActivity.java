package com.example.maceradores.maceracion.activities;

import android.content.ContentValues;
import android.content.Intent;
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
import com.example.maceradores.maceracion.adapters.ViewPagerAdapter;
import com.example.maceradores.maceracion.db.DatabaseHelper;
import com.example.maceradores.maceracion.models.Experiment;

public class CurrentExperienceActivity extends AppCompatActivity{
    //Data
    private int idMash;
    private String nameMash;

    //UI
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_experience);
        setToolbar();
        setTabLayout();
        setViewPager();
        setListenerTabLayout(viewPager);

        Intent intent = getIntent();
        if( intent.hasExtra("idMash") && intent.hasExtra("nameMash")){
            idMash = intent.getIntExtra("idMash", 0);
            nameMash = intent.getStringExtra("nameMash");
            setTitle("Medición " + nameMash);
            long newExperimentId = insertNewExperiment(idMash);
            if(newExperimentId == -1) {
                Toast.makeText(this, "Error al insertar experiencia", Toast.LENGTH_SHORT).show();
                // Me vuelvo al activity que lista las experiencias
                Intent intentError = new Intent(CurrentExperienceActivity.this, ExperimentActivity.class);
                intentError.putExtra("idMash", idMash);
                intentError.putExtra("nameMash", nameMash);
                startActivity(intentError);
            }
        } else {
            Toast.makeText(this, "Usted ha llegado aqui de una manera misteriosa", Toast.LENGTH_SHORT).show();
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
