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

                /*Data output = new Data.Builder() // Armar datos a devolver desde MyWork
                .putString(EXTRA_OUTPUT_MESSAGE, "I have come from MyWorker!")
                .build();*/
        //setOutputData(output);//Envía el MyData

        Log.d("El idExp es:",IdExp);
        int IdExp_int = Integer.valueOf(IdExp);

        //int cicleNumber = getInputData().getInt(NUMBEROFCICLES, 1);*/
       /* for (int i = 0; i < cicleNumber; ++i) {

            //int IdExp = getInputData().getInt(IDEXP,-1);
            //String IdList = getInputData().getString(IDLIST);
            //Log.d("El idExp es:",Integer.toString(IdExp));
            //Log.d("La lista es:",IdList);
            //getSensedValues(IdExp, IdList);

           *//* try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*//*
            //Llamada a Retrofit y a base de datos

        }*/


        //SystemClock.sleep(7000);
        insertExperimentHardCode(69,1);
        List<SensedValuesContainer> Lista = getSensedValues(IdExp_int,""); // Call to API to get the Sensed Values
        if(!Lista.isEmpty()) {
            for (SensedValuesContainer value : Lista) {
                Log.d("Un valor de temperatura", value.getTemp1());
                insertSensedValue(value);
            }
        }
        String insertedValues = getListIdInsertedSensedValue(69);
        Log.d("List of inserted val:", insertedValues);
        return Result.SUCCESS;
    }


    private List<SensedValuesContainer> getSensedValues(int idExp, String IdList) {
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
        jsonObject.addProperty("idExp", "69");
        jsonObject.addProperty("duracion_min", "1");

        Api api = retrofit.create(Api.class);
        Call<List<SensedValuesContainer>> call = api.getSensedValues(jsonObject);
        final List<SensedValuesContainer> Lista = new ArrayList<SensedValuesContainer>();
        call.enqueue(new Callback<List<SensedValuesContainer>>() {
            @Override
            public void onResponse(Call<List<SensedValuesContainer>> call, Response<List<SensedValuesContainer>> response) {
                List<SensedValuesContainer> values = response.body();
                for (SensedValuesContainer value : values) {
                   Lista.add(value);
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        });

        return Lista;
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


    public void sendNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1, notification.build());
    }


}