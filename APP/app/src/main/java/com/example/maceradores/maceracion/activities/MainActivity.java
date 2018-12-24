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
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.RetrofitGsonContainer.TempPh;
import com.example.maceradores.maceracion.adapters.MashListAdapter;
import com.example.maceradores.maceracion.db.DatabaseHelper;
import com.example.maceradores.maceracion.models.Mash;
import com.example.maceradores.maceracion.models.SensedValues;
import com.example.maceradores.maceracion.retrofitInterface.Api;

import java.util.ArrayList;
import java.util.List;
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
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                dbHelper.deleteDatabase();
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


    private void showAlertCurrentValues(){
        //---Hardcodeo para pruebas
      /*  float[] anArray = new float[5];

        anArray[0] = 50;
        anArray[1] = 49;
        anArray[2] = 51;
        anArray[3] = 48;
        anArray[4] = 5;
        List<Integer> lista = getListIdInsertedSensedValue(1);
        for(int j =0;j<lista.size();j++) {
            updateSensedValue(lista.get(j),anArray);
        }
        anArray[0] = 50;
        anArray[1] = 49;
        anArray[2] = 51;
        anArray[3] = 48;
        anArray[4] = 5;
        List<Integer> lista1 = getListIdInsertedSensedValue(2);
        for(int j =0;j<lista.size();j++) {
            updateSensedValue(lista.get(j),anArray);
        }
        anArray[0] = 50;
        anArray[1] = 49;
        anArray[2] = 51;
        anArray[3] = 48;
        anArray[4] = 5;
        List<Integer> lista2 = getListIdInsertedSensedValue(3);
        for(int j =0;j<lista.size();j++) {
            updateSensedValue(lista.get(j),anArray);
        }
        anArray[0] = 50;
        anArray[1] = 49;
        anArray[2] = 51;
        anArray[3] = 48;
        anArray[4] = 5;
        List<Integer> lista3 = getListIdInsertedSensedValue(4);
        for(int j =0;j<lista.size();j++) {
            updateSensedValue(lista.get(j),anArray);
        }*/

        //----------------------------

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
                tvCurrentValues.append("Temperatura: " + String.valueOf(temp) + "\n");
                tvCurrentValues.append("pH: " + tempPh.getPh() + "\n");
                tvCurrentValues.append("Temp. Ambiente: " + tempPh.getTempAmb() + "\n");
                tvCurrentValues.append("Humedad Ambiente: " + tempPh.getHumidity() + "\n");
                tvCurrentValues.append("Segunda Temp: " + tempPh.getTemp5() + "\n");

                //Oculto el ProgressBar y muestro el Texto
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                tvCurrentValues.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<TempPh> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();
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
    //---Funciones para pruebas
    private List<Integer> getListIdInsertedSensedValue(int idExp){
        //StringBuilder buffer = new StringBuilder();
        List<Integer> buffer= new ArrayList<>();

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {"idRaspi"};
        String selection = "id_exp = ?";
        String[] selectionArgs = { String.valueOf(idExp)};

        Cursor cursor = db.query("SensedValues", columns, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()){
            //buffer.append( cursor.getString(0)); // checkear si la columna es 0 o 1
            while(cursor.moveToNext()){
//                buffer.append(",");
//                buffer.append(cursor.getString(0));
                buffer.add(cursor.getInt(0));
            }
        }
        cursor.close();
        db.close();
//        return buffer.toString();
        return buffer;
    }


    private void updateSensedValue( int idSV, float[] v ){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues sensedValues = new ContentValues();

        sensedValues.put("temp1", v[0] );
        sensedValues.put("temp2", v[1] );
        sensedValues.put("temp3", v[2] );
        sensedValues.put("temp4", v[3] );
        sensedValues.put("pH", v[4] );

        String whereClausule = "idRaspi = ?";
        String[] whereClausuleArgs = new String[] {String.valueOf(idSV)};
        int cant = db.update("SensedValues", sensedValues,whereClausule, whereClausuleArgs);
        dbHelper.close();
    }

    void testEnzymeFunctions(){
        List<Pair<Float,Float>> tph = new ArrayList<>();
        tph.add( new Pair<Float, Float>(72f,5.4f));
        tph.add( new Pair<Float, Float>(63f,5.4f));
        tph.add( new Pair<Float, Float>(40f,5f));
        tph.add( new Pair<Float, Float>(50f,4.9f));

        float x;
        for( int i=0; i < tph.size(); i++){

            x = SensedValues.alphaAmylase( tph.get(i).first, tph.get(i).second );
            x = SensedValues.betaAmylase( tph.get(i).first, tph.get(i).second );
            x = SensedValues.betaGlucanase( tph.get(i).first, tph.get(i).second );
            x = SensedValues.protease( tph.get(i).first, tph.get(i).second );
        }
    }

} //end MainActivity
