package com.example.maceradores.maceracion.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maceradores.maceracion.Fragments.MeasureFragment;
import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.RetrofitGsonContainer.TempPh;
import com.example.maceradores.maceracion.adapters.MashListAdapter;
import com.example.maceradores.maceracion.db.DatabaseHelper;
import com.example.maceradores.maceracion.models.Mash;
import com.example.maceradores.maceracion.models.MeasureInterval;
import com.example.maceradores.maceracion.models.SensedValues;
import com.example.maceradores.maceracion.retrofitInterface.Api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMash);

        //añadir un alertDialog al fab con los valores actuales de los sensores.
        fab = (FloatingActionButton) findViewById(R.id.fabCurrentValues);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertCurrentValues();
            }
        });

        setToolbar();



    } //end OnCreate

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();

        // aca hago la consulta a la base de datos y la muestro en el recycler.
        mashList = getAllMash();


        rvAdapter = new MashListAdapter(mashList, R.layout.item_list_mash, new MashListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Mash mash, int position) {
                Intent intent = new Intent(MainActivity.this, ExperimentActivity.class);
                intent.putExtra("idMash", mash.getId());
                intent.putExtra("nameMash", mash.getName());
                startActivity(intent);
            }
        });
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private List<Mash> getAllMash() {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        List<Mash> results = dbHelper.getAllMash();
        dbHelper.close();
        return results;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //tengo que añadirle el layout que le cree.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addNewMash:
                // if clickeo add new mash, should appears a new activity for planning it.
                //Toast.makeText(this, "Click agregar maceracion", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, PlanningActivity.class);
                // si es necesario pasar algun parametro con putExtra.
                startActivity(intent);
                //finish();//No anda...
                return true;

            case R.id.deleteDatabase:
                //DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                //dbHelper.deleteDatabase();
                showAlertDeleteDatabase();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)//Esto es para que me deje usar el Toolbar q empieza e la APU 24
    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_MainActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().show();
    }

    private void showAlertDeleteDatabase(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Base de datos");
        builder.setMessage("¿Está seguro que desea eliminar la base de datos?");

        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // llamo a la función que me elimina la base de datos
                deleteDatabase();
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

    private void deleteDatabase(){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        dbHelper.deleteDatabase();
        dbHelper.close();

        mashList.clear();
        rvAdapter.notifyDataSetChanged();
    }

    private void showAlertCurrentValues(){
   /*     DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        List<SensedValues>lista=new ArrayList<>();
        lista = dbHelper.getAllSensedValues(12);
        lista = dbHelper.getAllSensedValues(13);
        lista = dbHelper.getAllSensedValues(14);
        dbHelper.deleteExperiment(12);
        dbHelper.deleteExperiment(13);
        dbHelper.deleteExperiment(14);
        if(lista.isEmpty())Log.d("No hay","SV");
        for(int i=0;i<lista.size();i++){
            Log.d("id_Raspi",String.valueOf(lista.get(i).getIdRaspi()));
            Log.d("id",String.valueOf(lista.get(i).getId()));
        }
        //cancelAllWork();*/
        //----------------------------------------

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Valores Actuales");

        View currentValuesView = LayoutInflater.from(this).inflate(R.layout.dialog_current_values, null);
        builder.setView(currentValuesView);

        //Ahora tenemos que obtener las referencia y cargarla
        final TextView tvCurrentValues = (TextView) currentValuesView.findViewById(R.id.tv_current_values);
        //Obtengo la referencia al progressbar
        //ProgressBar
        final ProgressBar mLoadingIndicator = (ProgressBar) currentValuesView.findViewById(R.id.pb_loading_indicator);


        //Muestro el Progressbar y oculto el texto
        mLoadingIndicator.setVisibility(View.VISIBLE);
        tvCurrentValues.setVisibility(View.INVISIBLE);

        //FIX DEL WAITING TIME para que sea de 1 minuto
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(240, TimeUnit.SECONDS)
                .writeTimeout(240, TimeUnit.SECONDS)
                .build();

        //Luego lo agrego a la llamada de Retrofit

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<TempPh> call = api.getTempPh();

        call.enqueue(new Callback<TempPh>() {
            @Override
            public void onResponse(Call<TempPh> call, Response<TempPh> response) {
                TempPh tempPh = response.body();
                //agrego los valores obtenidos a los textviews
                float temp=validatedTempMean(Float.valueOf(tempPh.getTemp1()),Float.valueOf(tempPh.getTemp2()),
                        Float.valueOf(tempPh.getTemp3()),Float.valueOf(tempPh.getTemp4()));
                String tempValid = String.format("%.2f", temp);
                tvCurrentValues.append("Temperatura: " + tempValid + "\n");
                tvCurrentValues.append("pH: " + String.format("%.2f", Float.valueOf(tempPh.getPh())) + "\n");
                tvCurrentValues.append("Temp. Ambiente: " + String.format("%.2f", Float.valueOf(tempPh.getTempAmb()))  + "\n");
                tvCurrentValues.append("Humedad Ambiente: " + String.format("%.2f", Float.valueOf(tempPh.getHumidity()))  + "\n");
                tvCurrentValues.append("Segunda Temp: " + String.format("%.2f", Float.valueOf(tempPh.getTemp5()))  + "\n");

                if(temp<0 || Float.valueOf(tempPh.getPh())<0||
                        Float.valueOf(tempPh.getTempAmb())<0||Float.valueOf(tempPh.getTemp5())<0){
                    Toast.makeText(getApplicationContext(), "Falla en algún/os Sensor/es",Toast.LENGTH_LONG).show();
                }
                //Oculto el ProgressBar y muestro el Texto
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                tvCurrentValues.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<TempPh> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error de Conexión con el Servidor",Toast.LENGTH_LONG).show();
            }
        });


        //agrego boton para cerrar el dialogo
        builder.setPositiveButton("LISTO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();

    }

    private float validatedTempMean(float t1, float t2, float t3, float t4){
        int divisor=4;
        float dividendo=0;
        if(t1==-1000){
            divisor--;
        }else{dividendo = dividendo + t1;}
        if(t2==-1000){
            divisor--;
        }else{dividendo = dividendo + t2;}
        if(t3==-1000){
            divisor--;
        }else{dividendo = dividendo + t3;}
        if(t4==-1000){
            divisor--;
        }else{dividendo = dividendo + t4;}
        if(dividendo==0){//Asi evito la division por cero
            return -1000;//Devuelvo, una temperatura invalida
        }else {
            float promedio = dividendo / divisor;
            return promedio;
        }

    }

    private void alterExperiment( int idExpOrigin, int idExpDestination){
        // aca la movida va a ser asi:
        // suponiendo que el experimento origen esta en optimas condiciones.
        // yo voy a tomar valores de temperatura y pH del experimento origen.
        // y los voy a meter en el correspondiente sensedvalues del experimento destino.
        // con una modificación..

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        //me traigo todos los sensedvalues de este experimento.
        List<SensedValues> svList = dbHelper.getAllSensedValues(idExpOrigin);

        // ahora me traigo los ids de los sensedvalues insertados en el experimento destino.
        List<Integer> svIdListDestination = dbHelper.getAllSensedValuesId(idExpDestination);

        for( int i=0; i < svList.size(); i++){
            SensedValues currentSv = svList.get(i);
            //modifico los valores.
            float rangoModTemp = 0.3f;
            float rangoModPh = 0.1f;

            float[] valoresMod = new float[]{
                    alterValue(currentSv.getTemp1(), rangoModTemp),
                    alterValue(currentSv.getTemp2(), rangoModTemp),
                    alterValue(currentSv.getTemp3(), rangoModTemp),
                    alterValue(currentSv.getTemp4(), rangoModTemp),
                    alterValue(currentSv.getTempSecondary(), rangoModTemp),
                    alterValue(currentSv.getTempPH(), rangoModTemp),
                    alterValue(currentSv.getpH(), rangoModPh)
            };

            //ahora actualizo los valores del sensesvalues.
            dbHelper.updateSensedValue(svIdListDestination.get(i), valoresMod);
        }

        dbHelper.close();
    }

    private float alterValue( float value, float range){
        // Aca la onda es el valor que me entra lo tengo que alterar dentro del rango que le pido.
        Random rand = new Random();
        float alt = rand.nextFloat()*range - (range/2.0f);
        return value + alt;
    }

    private void enhanceTemperatureExperiment(int idExp){
        // mi idea sería acomodar la temperatura un experimento para que quede
        // como la planificación.
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        //primero debería obtener la planificación de la maceración.
        //obtengo el id de la maceración corresóndiente.
        int idMash = dbHelper.getIdMash(idExp);

        //obtengo la temperatura a la que la tengo que llevar.
        List<MeasureInterval> intervals = dbHelper.getMeasureIntervals(idMash);
        float tempPlanned = intervals.get(0).getMainTemperature(); //suponiedo que es simple
        intervals.clear();

        //obtengo todos los sensedValues del experimento.
        List<SensedValues> svList = dbHelper.getAllSensedValues(idExp);

        //voy a recorrer todos los sensed values y voy acumulando los valores de temperatura para
        //despues obtener el promedio.

        float acumulado = 0;
        for(SensedValues sv: svList){
            float t = validatedTempMean( sv.getTemp1(),sv.getTemp2(), sv.getTemp3(), sv.getTemp4());
            acumulado = acumulado + t;
        }

        float promedio = acumulado / svList.size();
        float desvio = tempPlanned - promedio;

        //ahora debería ir recorriendo los senseedvalues e ir modificando los valores.
        //para actualizarlos.
        for(SensedValues sv : svList){
            //reutilizo la función que había definido antes.
            float[] valoresMod = new float[]{
                    sv.getTemp1() + desvio,
                    sv.getTemp2() + desvio,
                    sv.getTemp3() + desvio,
                    sv.getTemp4() + desvio,
                    sv.getTempSecondary(),
                    sv.getTempPH(),
                    sv.getpH()
            };

            dbHelper.updateSensedValue(sv.getId(), valoresMod);
        }

        dbHelper.close();
    }

} //end MainActivity
