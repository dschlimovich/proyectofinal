package com.example.maceradores.maceracion.WorkManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.RetrofitGsonContainer.SensedValuesContainer;
import com.example.maceradores.maceracion.activities.MainActivity;
import com.example.maceradores.maceracion.db.DatabaseHelper;
import com.example.maceradores.maceracion.retrofitInterface.Api;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyWorker extends Worker {
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_TEXT = "text";
    public static final String EXTRA_OUTPUT_MESSAGE = "output_message";
//    public static final String NUMBEROFCICLES = "1";
    public static final String IDEXP = "-1";


    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        String IdExp = getInputData().getString(IDEXP);

        Log.d("El idExp es:",IdExp);
        int IdExp_int = Integer.valueOf(IdExp);

        int idMash = getIdMashByIdExp(IdExp_int);
        int intervaloMedicion = intervaloMedicion(idMash); //intervalo de medicion en segundos! o segun convengamos

        int NumberOfCalls = cantMediciones(idMash, intervaloMedicion );
        int counter = 0;
        while (counter<NumberOfCalls){

            String AppList = getListIdInsertedSensedValue(IdExp_int); // Get the sensed values in the APP DB
            getSensedValues(IdExp_int,AppList); // Call to API to get the Sensed Values

            counter++;
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


        //SystemClock.sleep(7000);
        //insertExperimentHardCode(69,1);



        return Result.SUCCESS;
    }

    private int getIdMashByIdExp( int idExp){
        int idMash = -1;

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {"maceracion"};
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(idExp)};

        Cursor cursor = db.query("Experimento", columns, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()){
            idMash = cursor.getInt(0); //como tengo una sola columna, devuelvo la primera nomas.
        }
        cursor.close();
        db.close();

        return idMash;
    }

    private int intervaloMedicion (int idMash){
        int intervaloMedicion = 0;
        // saber el periodo de medicion.
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {"intervaloMedTemp"};
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(idMash)};

        Cursor cursor = db.query("Maceracion", columns, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()){
            intervaloMedicion = cursor.getInt(0); //como tengo una sola columna, devuelvo la primera nomas.
        }
        cursor.close();
        db.close();

        return intervaloMedicion;
    }

    private int cantMediciones( int idMash, int intervaloMedicion){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(duracion) FROM Intervalo WHERE maceracion = ?", new String[]{String.valueOf(idMash)});
        int duracionTotal = 0;
        if(c.moveToFirst()){
               duracionTotal = c.getInt(0);
        }
        c.close();
        db.close();
        return duracionTotal / (intervaloMedicion/2);
    }

    private void getSensedValues(int idExp, String IdList) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(240, TimeUnit.SECONDS)
                .writeTimeout(240, TimeUnit.SECONDS)
                .build();

        //Luego lo agrego a la llamada de Retrofit

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        //------Build new JsonObject with Experiment to be send
        //{ "idExp": "31", "ArrayID": "" }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idExp", String.valueOf(idExp));
        jsonObject.addProperty("ArrayID", IdList);

        Api api = retrofit.create(Api.class);
        Call<List<SensedValuesContainer>> call = api.getSensedValues(jsonObject);
//        final List<SensedValuesContainer> Lista = new ArrayList<SensedValuesContainer>();
        call.enqueue(new Callback<List<SensedValuesContainer>>() {
            @Override
            public void onResponse(Call<List<SensedValuesContainer>> call, Response<List<SensedValuesContainer>> response) {
                List<SensedValuesContainer> values = response.body();
                if (!values.isEmpty()) { // Only makes Insertions id the response is not empty
                    for (SensedValuesContainer value : values) {
                        Log.d("Un valor...", value.getTemp1());
                        Long flag = insertSensedValue(value); // Here we insert the values
                        if(flag==-1)Log.d("Error en","Inserción");
                    }
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private long insertSensedValue(SensedValuesContainer svc){
// creo la instancia de basede datos para insertar.
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_exp", Integer.valueOf(svc.getId_exp()));
        values.put("id", Integer.valueOf(svc.getId()));
        values.put("fechayhora", svc.getFechayhora());  //Aca no se que movida. Es tipo fecha.
        values.put("temp1", Float.valueOf(svc.getTemp1()));
        values.put("temp2", Float.valueOf(svc.getTemp2()));
        values.put("temp3", Float.valueOf(svc.getTemp3()));
        values.put("temp4", Float.valueOf(svc.getTemp4()));
        values.put("temp5", Float.valueOf(svc.getTemp5()));
        values.put("tempPh", Float.valueOf(svc.getTempPh()));
        values.put("tempAmb", Float.valueOf(svc.getTempAmb()));
        values.put("pH", Float.valueOf(svc.getpH()));

        long newSensedValueId = db.insert("SensedValues", null, values);
        dbHelper.close();
        return newSensedValueId; //si devuelve -1 es porque no pudo insertar
    }

    private void insertExperimentHardCode(int idExp, int idMash){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", idExp);
        values.put("maceracion", idMash); //Suponete que tenes las maceracion con id 1 y se la agrego a esa.
        long newExperimentId = db.insert("Experimento", null, values);
        dbHelper.close();
    }

    private String getListIdInsertedSensedValue(int idExp){
        StringBuilder buffer = new StringBuilder();

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {"id"};
        String selection = "id_exp = ?";
        String[] selectionArgs = { String.valueOf(idExp)};

        Cursor cursor = db.query("SensedValues", columns, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()){
            buffer.append( cursor.getString(0)); // checkear si la columna es 0 o 1
            while(cursor.moveToNext()){
                buffer.append(",");
                buffer.append(cursor.getString(0));
            }
        }
        cursor.close();
        db.close();
        return buffer.toString();
    }

}