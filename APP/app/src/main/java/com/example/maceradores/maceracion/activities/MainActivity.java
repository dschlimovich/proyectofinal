package com.example.maceradores.maceracion.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.example.maceradores.maceracion.models.MeasureInterval;
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

    } //end OnCreate

    @Override
    protected void onResume() {
        super.onResume();

        // aca hago la consulta a la base de datos y la muestro en el recycler.
        mashList = getAllMash();

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

        setToolbar();
    }

    private List<Mash> getAllMash() {
        List<Mash> resultados = new ArrayList<Mash>();
        // tengo que hacer una consulta SQL.
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "id",
                "nombre"
        };
        //String selection = "id = ?";
        //String[] selectionArgs = { String.valueOf(newRowId)};

        Cursor cursor = db.query("Maceracion", projection, null, null, null, null, null);

        //List itemNames = new ArrayList<>();
        while(cursor.moveToNext()) {
            String itemName = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre"));
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
            );

            resultados.add(new Mash(id, itemName));
            //itemNames.add(itemName);
        }
        cursor.close();

        return resultados;
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
                tvCurrentValues.append("Temperatura: " + tempPh.getTemp1() + "\n");
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

    private void testBD(){
        // Make a method which write and read from the database.
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Ahora puedo escribir en la base de datos,
        ContentValues values = new ContentValues();
        values.put("nombre", "test SQLite"); //el nombre tiene la clausula unique
        values.put( "tipo", "simple");
        values.put("volumen", 1);
        values.put( "intervaloMedTemp", 1);
        values.put("intervaloMedPh", 2);

        long newRowId = db.insert("Maceracion", null, values);

        // cuenta la leyenda que en newRowId tengo el id del ultimo valor insertado.
        if( newRowId != -1){
            Toast.makeText(this, "Inserto sin problemas", Toast.LENGTH_SHORT).show();
            // si lo inserto, veamos que pueda obtener lo que acabo de insertar.
            db = dbHelper.getReadableDatabase();
            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    "id",
                    "nombre",
                    "tipo"
            };

            // Filter results WHERE "title" = 'My Title'
            String selection = "id = ?";
            String[] selectionArgs = { String.valueOf(newRowId)};

            Cursor cursor = db.query("Maceracion", projection, selection, selectionArgs, null, null, null);

            List itemNames = new ArrayList<>();
            while(cursor.moveToNext()) {
                String itemName = cursor.getString(
                        cursor.getColumnIndexOrThrow("nombre"));
                itemNames.add(itemName);
            }
            cursor.close();

            if(itemNames.size() > 0){
                Toast.makeText(this, itemNames.get(0).toString(), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "No pudo leer el valor recien insertado", Toast.LENGTH_SHORT).show();
            }
             //me quedaría eliminarlo para que quede limpia la bd
            int cant_eliminados = db.delete("Maceracion", selection, selectionArgs);
            if( cant_eliminados == 1){
                Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "No se elimino nada", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Hubo problemas", Toast.LENGTH_SHORT).show();
        }

        //al final tengo que cerrar la base de datos. En verdad esto iria en el metodo onDestroy
        dbHelper.close();

    }

} //end MainActivity
