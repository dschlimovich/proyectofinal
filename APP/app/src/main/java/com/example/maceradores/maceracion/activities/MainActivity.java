package com.example.maceradores.maceracion.activities;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.adapters.MashListAdapter;
import com.example.maceradores.maceracion.models.Mash;
import com.example.maceradores.maceracion.models.MeasureInterval;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Variables del modelo
    private List<Mash> mashList;

    // UI elements.
    //List Mash
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //Current values
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Mostrar todos los tipos de maceraciones que tiene planficado el buen hombre.
        mashList = hardcodeMashList();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMash);
        rvAdapter = new MashListAdapter(mashList, R.layout.item_list_mash, new MashListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Mash mash, int position) {
                Toast.makeText(MainActivity.this, mash.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        //añadir un alertDialog al fab con los valores actuales de los sensores.
        fab = (FloatingActionButton) findViewById(R.id.fabCurrentValues);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertCurrentValues();
            }
        });
    }

    private List<Mash> hardcodeMashList() {
        final MeasureInterval measureAux = new MeasureInterval(70, 70, 5.4f, 60, 2,2);
        final List<MeasureInterval> listAux = new ArrayList<MeasureInterval>(){{ add(measureAux);}};
        return new ArrayList<Mash>(){{
           add(new Mash(0, "Mash 1", listAux));
            add(new Mash(0, "Mash 2", listAux));
        }};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //tengo que añadirle el layout que le cree.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void showAlertCurrentValues(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Valores Actuales");

        View currentValuesView = LayoutInflater.from(this).inflate(R.layout.dialog_current_values, null);
        builder.setView(currentValuesView);

        //Obtenemos los valores que se van a mostrar en el alert dialog.
        //HARDCODE
        String temperature = "68°C";
        String ph = "5.4";
        String tempEnviroment = "20°C";
        String humidity = "88%";
        String secondTemperature = "-";
        // TODO obtener valores de los sensores a traves de la API
        //Ahora tenemos que obtener las referencias y cargarlas
        TextView tvCurrentTemperature = (TextView) currentValuesView.findViewById(R.id.tvDialogCurrentTemperature);
        TextView tvCurrentPh = (TextView) currentValuesView.findViewById(R.id.tvDialogCurrentPh);
        TextView tvCurrentTempEnv = (TextView) currentValuesView.findViewById(R.id.tvDialogCurrentTempEnviroment);
        TextView tvCurrentHumidity = (TextView) currentValuesView.findViewById(R.id.tvDialogCurrentHumidity);
        TextView tvCurrentSecondTemperature = (TextView) currentValuesView.findViewById(R.id.tvDialogCurrentSecondTemperature);

        //agrego los valores obtenidos a los textviews
        tvCurrentTemperature.append(temperature);
        tvCurrentPh.append(ph);
        tvCurrentTempEnv.append(tempEnviroment);
        tvCurrentHumidity.append(humidity);
        tvCurrentSecondTemperature.append(secondTemperature);

        //agrego boton para cerrar el dialogo
        builder.setPositiveButton("LISTO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }
}
