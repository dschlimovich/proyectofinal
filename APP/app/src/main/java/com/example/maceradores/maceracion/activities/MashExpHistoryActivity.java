package com.example.maceradores.maceracion.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.adapters.ViewPagerAdapter;
import com.example.maceradores.maceracion.adapters.ViewPagerAdapter2;

import java.util.UUID;

public class MashExpHistoryActivity extends AppCompatActivity {



    //UI
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter2 adapter;
    private UUID workId;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mash_exp_history);

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
        adapter = new ViewPagerAdapter2(getSupportFragmentManager(),this,tabLayout.getTabCount());
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
