package com.example.maceradores.maceracion.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.maceradores.maceracion.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //tengo que añadirle el layout que le cree.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
