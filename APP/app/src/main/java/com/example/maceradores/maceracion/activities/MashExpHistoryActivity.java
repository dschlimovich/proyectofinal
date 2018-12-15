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

import java.util.UUID;

public class MashExpHistoryActivity extends AppCompatActivity {



    //UI
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapterStatics adapter;
    private UUID workId;

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

}
