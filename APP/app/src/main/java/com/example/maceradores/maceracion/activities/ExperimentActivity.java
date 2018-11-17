package com.example.maceradores.maceracion.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.maceradores.maceracion.R;

public class ExperimentActivity extends AppCompatActivity {
    // Data
    private int idMash;

    //UI
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if(intent.hasExtra("idMash")){
            //Toast.makeText(ExperimentActivity.this, "" + intent.getIntExtra("idMash", -1), Toast.LENGTH_SHORT).show();
            this.idMash = intent.getIntExtra("idMash", -1);
        }
        else{
            startActivity(new Intent(ExperimentActivity.this, MainActivity.class));
        }
        if(intent.hasExtra("nameMash")){
            setTitle(intent.getStringExtra("nameMash"));
        }

        fab = (FloatingActionButton) findViewById(R.id.fabCurrentValues);
        fab.setImageResource(android.R.drawable.ic_media_play);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //ahora cargo los datos relacionados a los experimentos.
    }
}//end ExperimentActivity
