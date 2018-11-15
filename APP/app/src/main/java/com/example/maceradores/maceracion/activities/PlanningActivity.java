package com.example.maceradores.maceracion.activities;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.adapters.GrainListAdapter;
import com.example.maceradores.maceracion.adapters.IntervalListAdapter;
import com.example.maceradores.maceracion.models.Grain;
import com.example.maceradores.maceracion.models.Mash;
import com.example.maceradores.maceracion.models.MeasureInterval;

import java.util.ArrayList;
import java.util.List;

public class PlanningActivity extends AppCompatActivity {
    //Buttons
    Button buttonAddGrain;
    FloatingActionButton fab;

    //Container
    ListView listGrains;
    GrainListAdapter grainListAdapter;

    RecyclerView listsIntervals;
    RecyclerView.LayoutManager layoutManager;
    IntervalListAdapter intervalListAdapter;

    //Data
    List<MeasureInterval> intervals;
    List<Grain> grains;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);
        // por ahora supongo que aca llega desde el mainactivity.

        Spinner spinner = (Spinner) findViewById(R.id.spinnerTiposMaceracion);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tiposMaceracion, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // List of Grains
        grains = new ArrayList<Grain>();
        grains.add(new Grain("Prueba", 0.5f, 0.5f));
        listGrains = (ListView) findViewById(R.id.listViewGrains);
        grainListAdapter = new GrainListAdapter(this, grains, R.layout.item_list_grain);
        listGrains.setAdapter(grainListAdapter);
        registerForContextMenu(this.listGrains);

        // List of intervals
        intervals = new ArrayList<MeasureInterval>();
        intervals.add(new MeasureInterval(1,1,1,1,1,1,1,1));
        layoutManager = new LinearLayoutManager(this);
        intervalListAdapter = new IntervalListAdapter(intervals, R.layout.item_list_interval, new IntervalListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(MeasureInterval interval, int position) {
                Toast.makeText(PlanningActivity.this, "Intervalo Clickeado", Toast.LENGTH_SHORT).show();
            }
        });
        listsIntervals = (RecyclerView) findViewById(R.id.recyclerViewIntervalPlanning);
        listsIntervals.setAdapter(intervalListAdapter);
        listsIntervals.setLayoutManager(layoutManager);
        //listsIntervals.setHasFixedSize(true);

        //Add grain
        buttonAddGrain = findViewById(R.id.buttonAddGrain);
        buttonAddGrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogAddGrain();
            }
        });

        //Add measure Interval
        this.intervals = new ArrayList<MeasureInterval>();
        fab = (FloatingActionButton) findViewById(R.id.fabAddMeasureInterval);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogAddMeasureInterval();
            }
        });

    } //end onCreate

    private void showAlertDialogAddMeasureInterval() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nueva etapa");

        View addIntervalView = LayoutInflater.from(this).inflate(R.layout.dialog_add_measure_interval, null);
        builder.setView(addIntervalView);

        //le pongo el numerito de etapa que iria a agregar.
        TextView numberInterval = (TextView) addIntervalView.findViewById(R.id.textViewNumberInterval);
        numberInterval.append(String.valueOf(intervals.size() + 1));

        //Necesito todas las referencias a los editText.
        final EditText duration = (EditText) addIntervalView.findViewById(R.id.editTextAddIntervalDuration);
        final EditText temperature = (EditText) addIntervalView.findViewById(R.id.editTextAddIntervalTemperature);
        final EditText temperatureDeviation = (EditText) addIntervalView.findViewById(R.id.editTextAddIntervalTemperatureDeviation);
        final EditText ph = (EditText) addIntervalView.findViewById(R.id.editTextAddIntervalPh);
        final EditText phDeviation = (EditText) addIntervalView.findViewById(R.id.editTextAddIntervalPhDeviation);
        final EditText tempDecoccion = (EditText) addIntervalView.findViewById(R.id.editTextAddIntervalTemperatureDecoccion);
        final EditText tempDecoccionDeviation = (EditText) addIntervalView.findViewById(R.id.editTextAddIntervalTemperatureDecoccionDeviation);

        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Aca debo tomar los valores y usarlos para llenar un IntervalMeasure.
                //public MeasureInterval(float mainTemperature, float mainTemperatureDeviation, float secondTemperature, float secondTemperatureDeviation, float pH, float phDeviation, int duration)
                MeasureInterval interval = new MeasureInterval(
                        Float.valueOf(temperature.getText().toString().trim()),
                        Float.valueOf(temperatureDeviation.getText().toString().trim()),
                        Float.valueOf(tempDecoccion.getText().toString().trim()),
                        Float.valueOf(tempDecoccionDeviation.getText().toString().trim()),
                        Float.valueOf(ph.getText().toString().trim()),
                        Float.valueOf(phDeviation.getText().toString().trim()),
                        Integer.valueOf(duration.getText().toString().trim())
                );

                //Ahora tengo que agregarlo a la lista de intervalos
                intervals.add(interval);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle( this.grains.get(info.position).getName());

        inflater.inflate(R.menu.context_menu_grains, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //manejamos los eventos
        switch (item.getItemId()){
            case R.id.deleteGrainsContextMenu:
                // aprete en el context menu la opcion eliminar.
                //tengo que obtener la posicion en la que estoy y eliminarla.
                this.grains.remove(info.position);
                //notifico a los adaptadores
                this.grainListAdapter.notifyDataSetChanged();
                Toast.makeText(PlanningActivity.this, "Grano eliminado", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showAlertDialogAddGrain() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Grano");

        View addGrainView = LayoutInflater.from(this).inflate(R.layout.dialog_add_grain, null);
        builder.setView(addGrainView);

        final EditText grainName = (EditText) addGrainView.findViewById(R.id.editTextGrainName);
        final EditText grainQuantity = (EditText) addGrainView.findViewById(R.id.editTextGrainQuantity);
        final EditText grainYield = (EditText) addGrainView.findViewById(R.id.editTextGrainYield);

        builder.setPositiveButton("AGREGAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = grainName.getText().toString().trim();
                float quantity = Float.valueOf(grainQuantity.getText().toString());
                float yield = Float.valueOf(grainYield.getText().toString());

                Grain grain = new Grain(name, quantity, yield);
                grains.add(grain);

                grainListAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }
} //end Activity
