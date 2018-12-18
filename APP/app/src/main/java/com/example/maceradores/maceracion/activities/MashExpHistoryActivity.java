package com.example.maceradores.maceracion.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.adapters.ViewPagerAdapterStatics;
import com.example.maceradores.maceracion.db.DatabaseHelper;
import com.example.maceradores.maceracion.models.SensedValues;

import java.util.ArrayList;
import java.util.List;

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
            this.idMash = intent.getIntExtra("idMash", 0);
            setTitleName(idMash);
        }


        setToolbar();
        setTabLayout();
        setViewPager();
        setListenerTabLayout(viewPager);


    }

    private void setTabLayout(){
        tabLayout = (TabLayout) findViewById(R.id.tabLayout2);
        tabLayout.addTab(tabLayout.newTab().setText("GENERAL"));//Esta tiene la posicion 0
        tabLayout.addTab(tabLayout.newTab().setText("EXPERIMENTOS"));
    }

    private void setViewPager(){
        viewPager = (ViewPager) findViewById(R.id.viewPager2);
        adapter = new ViewPagerAdapterStatics(getSupportFragmentManager(),this,tabLayout.getTabCount(),this.idMash);
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)//Esto es para que me deje usar el Toolbar q empieza e la APU 24
    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_MashExpActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().show();
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
            setTitle("Maceración " + name);
        }
        cursor.close();
        db.close();
    }

}
