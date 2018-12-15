package com.example.maceradores.maceracion.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.adapters.ViewPagerAdapterStatics;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public class MashExpHistoryActivity extends AppCompatActivity {


    //UI
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapterStatics adapter;


    private int idMash;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mash_exp_history);


        Intent intent = getIntent();
        if(intent.hasExtra("idMash")){
            this.idMash = intent.getIntExtra("idMash", -1);

        }


        setToolbar();
        setTabLayout();
        setViewPager();

    }

    private void setTabLayout(){
        tabLayout = (TabLayout) findViewById(R.id.tabLayout2);
        tabLayout.addTab(tabLayout.newTab().setText("GENERAL"));//Esta tiene la posicion 0
        tabLayout.addTab(tabLayout.newTab().setText("EXPERIMENTOS"));
    }

    private void setViewPager(){
        viewPager = (ViewPager) findViewById(R.id.viewPager2);
        adapter = new ViewPagerAdapterStatics(getSupportFragmentManager(),this,tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)//Esto es para que me deje usar el Toolbar q empieza e la APU 24
    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_MashExpActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().show();
    }

    private ArrayList<Entry> MeanTempeatureSet(){

        //Primero hacer un select con todos los idExp relacionados a este idMash
        //Armar un Array q tenga tantas filas como idExp.
        //Recorrer los idExp haciendo Select de Sensed Values y ubicarlos en cada Fila de la Matriz.
        //Validar que la cant de Sensed Values q vienen por IdExp sea == a los q deberia de tener un Exp completo.
        
    ArrayList<Entry> retorno = new ArrayList<>();
    return retorno;

    }

}
