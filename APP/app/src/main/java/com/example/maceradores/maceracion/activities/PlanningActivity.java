package com.example.maceradores.maceracion.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import com.example.maceradores.maceracion.db.DatabaseHelper;
import com.example.maceradores.maceracion.models.Grain;
import com.example.maceradores.maceracion.models.Mash;
import com.example.maceradores.maceracion.models.MeasureInterval;

import java.util.ArrayList;
import java.util.List;

public class PlanningActivity extends AppCompatActivity {
    //Buttons
    private Button buttonAddGrain;
    private FloatingActionButton fab;

    //Container
    private ListView listGrains;
    private GrainListAdapter grainListAdapter;

    private RecyclerView listsIntervals;
    private RecyclerView.LayoutManager layoutManager;
    private IntervalListAdapter intervalListAdapter;

    //Data - Fields to create the new mash.
    private String type;
    private float volume;
    private float density;
    private List<MeasureInterval> intervals;
    private List<Grain> grains;

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
        //grains.add(new Grain("Prueba", 0.5f, 0.5f));
        listGrains = (ListView) findViewById(R.id.listViewPlanningGrains);
        grainListAdapter = new GrainListAdapter(this, grains, R.layout.item_list_grain);
        listGrains.setAdapter(grainListAdapter);
        registerForContextMenu(this.listGrains);

        // List of intervals
        intervals = new ArrayList<MeasureInterval>();
        layoutManager = new LinearLayoutManager(this);
        intervalListAdapter = new IntervalListAdapter(intervals, R.layout.item_list_interval, new IntervalListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(MeasureInterval interval, int position) {
                Toast.makeText(PlanningActivity.this, "Intervalo Borrado", Toast.LENGTH_SHORT).show();
                intervals.remove(position);
                //intervalListAdapter.notifyItemRemoved(position);
                intervalListAdapter.notifyDataSetChanged();
            }
        });
        listsIntervals = (RecyclerView) findViewById(R.id.recyclerViewIntervalPlanning);
        listsIntervals.setAdapter(intervalListAdapter);
        listsIntervals.setLayoutManager(layoutManager);
        //listsIntervals.setHasFixedSize(true);

        //Add grain
        buttonAddGrain = findViewById(R.id.buttonPlanningAddGrain);
        buttonAddGrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogAddGrain();
            }
        });

        //Add measure Interval
        fab = (FloatingActionButton) findViewById(R.id.fabAddMeasureInterval);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogAddMeasureInterval();
                //intervalListAdapter.notifyDataSetChanged();
            }
        });

        //Ocultar el teclado cuando arranca el activity... es bastante molesto
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    } //end onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_planning_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.acceptPlannification:
                //Me robo los valores de los editText correspondientes a volumen y densidad.
                EditText volumePlanning = findViewById(R.id.editTextPlanningVolumen);
                EditText densityPlanning = findViewById(R.id.editTextPlanningDensidad);
                this.volume = Float.valueOf(volumePlanning.getText().toString().trim());
                this.density = Float.valueOf(densityPlanning.getText().toString().trim());
                //y el correspondiente al tipo de maceracion.
                Spinner spinner = findViewById(R.id.spinnerTiposMaceracion);
                this.type = spinner.getSelectedItem().toString().trim();

                showAlertDialogFinishPlanning();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showAlertDialogFinishPlanning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Guardar Maceración");

        View finishPlanningView = LayoutInflater.from(this).inflate(R.layout.dialog_finish_planning, null);
        builder.setView(finishPlanningView);
        // Necesito las referecias a los editText
        final EditText nombre = (EditText) finishPlanningView.findViewById(R.id.editTextFinishPlanningName);
        final EditText medTemp = (EditText) finishPlanningView.findViewById(R.id.editTextFinishPlanningMedTemp);
        final EditText medPh = (EditText) finishPlanningView.findViewById(R.id.editTextFinishPlanningMedPh);

        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Este es el momento donde debería crear el mash.
                // Me robo los valores de los edit text
                String nameMash = nombre.getText().toString().trim();
                int periodoMedicionTemp = Integer.valueOf(medTemp.getText().toString().trim());
                int periodoMedicionPh = Integer.valueOf(medPh.getText().toString().trim());
                // In theory, i have all fields to create a new Mash.
                /*
                Mash newMash = new Mash(nameMash);
                newMash.setTipo(PlanningActivity.this.type);
                newMash.setPlan(PlanningActivity.this.intervals);
                newMash.setGrains(PlanningActivity.this.grains);
                newMash.setVolumen(PlanningActivity.this.volume);
                newMash.setDensidadObjetivo(PlanningActivity.this.density);
                newMash.setPeriodMeasureTemperature(periodoMedicionTemp);
                newMash.setPeriodMeasurePh(periodoMedicionPh); */

                //At this moment, i need to insert this new mash in the database
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //Ahora puedo escribir en la base de datos,
                ContentValues mashValues = new ContentValues();
                mashValues.put( "nombre", nameMash); //el nombre tiene la clausula unique
                mashValues.put( "tipo", PlanningActivity.this.type);
                mashValues.put( "volumen", PlanningActivity.this.volume);
                mashValues.put( "densidadObjetivo", PlanningActivity.this.density);
                mashValues.put( "intervaloMedTemp", periodoMedicionTemp);
                mashValues.put( "intervaloMedPh", periodoMedicionPh);

                long newMashId = db.insert("Maceracion", null, mashValues);

                // cuenta la leyenda que en newRowId tengo el id del ultimo valor insertado.
                if( newMashId != -1){
                    //Toast.makeText(PlanningActivity.this, "Inserto sin problemas", Toast.LENGTH_SHORT).show();
                    // Si inserto la maceración, tengo que insertar ademas los granos y las etapas de medicion.
                    // Comencemos por los granos.
                    ContentValues grainValues;
                    for(int i = 0; i < grains.size(); i++){
                        grainValues = new ContentValues();
                        grainValues.put("nombre", grains.get(i).getName());
                        grainValues.put("cantidad", grains.get(i).getQuantity());
                        grainValues.put("extractoPotencial", grains.get(i).getExtractPotential());
                        grainValues.put("maceracion", newMashId);

                        long newGrainId = db.insert("Grano",null, grainValues );
                        if(newGrainId == -1){
                            Toast.makeText(PlanningActivity.this, "Hubo problemas insertando este grano", Toast.LENGTH_SHORT).show();
                        }
                    } // end fir agregado de granos.

                    //Ahora agregamos los intervalos de medicion.
                    ContentValues intervalValues;
                    for( int i = 0; i < intervals.size(); i++){
                        intervalValues = new ContentValues();

                        intervalValues.put("duracion", intervals.get(i).getDuration());
                        intervalValues.put("temperatura", intervals.get(i).getMainTemperature());
                        intervalValues.put("desvioTemperatura", intervals.get(i).getMainTemperatureDeviation());
                        intervalValues.put("ph", intervals.get(i).getpH());
                        intervalValues.put("desvioPh", intervals.get(i).getPhDeviation());
                        intervalValues.put("tempDecoccion", intervals.get(i).getSecondTemperature());
                        intervalValues.put("desvioTempDecoccion", intervals.get(i).getSecondTemperatureDeviation());
                        intervalValues.put("maceracion", newMashId);

                        long newIntervalId = db.insert("Intervalo", null, intervalValues);
                        if(newIntervalId == -1)
                            Toast.makeText(PlanningActivity.this, "Problemas insertando intervalo", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(PlanningActivity.this, "Problemas al insertar", Toast.LENGTH_SHORT).show();
                }
                // chequeamos si me toma los cambios.
                dbHelper.close();
                startActivity(new Intent(PlanningActivity.this, MainActivity.class));
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

    private void showAlertDialogAddMeasureInterval() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo Intervalo");

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
                addInterval(interval);

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
        final EditText grainExtractPotential = (EditText) addGrainView.findViewById(R.id.editTextGrainExtractPotential);

        builder.setPositiveButton("AGREGAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = grainName.getText().toString().trim();
                float quantity = Float.valueOf(grainQuantity.getText().toString());
                float extractPotential = Float.valueOf(grainExtractPotential.getText().toString());

                Grain grain = new Grain(name, quantity, extractPotential);
                grains.add(grain);

                grainListAdapter.notifyDataSetChanged();
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

    private void addInterval(MeasureInterval interval){
        this.intervals.add(interval);
        this.intervalListAdapter.notifyDataSetChanged();
    }
} //end PlanningActivity
