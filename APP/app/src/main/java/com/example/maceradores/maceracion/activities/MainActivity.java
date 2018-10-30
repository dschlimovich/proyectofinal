package com.example.maceradores.maceracion.activities;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.example.maceradores.maceracion.R;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //añadir un alertDialog al fab con los valores de las mediciones.
        fab = (FloatingActionButton) findViewById(R.id.fabCurrentValues);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertCurrentValues("Valores Actuales", null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //tengo que añadirle el layout que le cree.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void showAlertCurrentValues(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(title != null) builder.setTitle(title);
        if(message != null) builder.setMessage(message);

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
