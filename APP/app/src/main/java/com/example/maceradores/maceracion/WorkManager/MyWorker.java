package com.example.maceradores.maceracion.WorkManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.maceradores.maceracion.R;
import com.example.maceradores.maceracion.RetrofitGsonContainer.SensedValuesContainer;
import com.example.maceradores.maceracion.activities.MainActivity;
import com.example.maceradores.maceracion.retrofitInterface.Api;
import com.google.gson.JsonObject;

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
    //    public static final String EXTRA_TITLE = "title";
//    public static final String EXTRA_TEXT = "text";
//    public static final String EXTRA_OUTPUT_MESSAGE = "output_message";
    public static final String NUMBEROFCICLES = "1";
    public static final String IDEXP = "-1";
    public static final String IDLIST = "";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
       /* String title = getInputData().getString(EXTRA_TITLE);
        String text = getInputData().getString(EXTRA_TEXT);
        //sendNotification("Simple Work Manager", "I have been send by WorkManager.");
        sendNotification(title, text);

        Data output = new Data.Builder() // Armar datos a devolver desde MyWork
                .putString(EXTRA_OUTPUT_MESSAGE, "I have come from MyWorker!")
                .build();*/

        //setOutputData(output);//Envía el MyData

        int cicleNumber = getInputData().getInt(NUMBEROFCICLES, 1);
        for (int i = 0; i < cicleNumber; i++) {

            int IdExp = getInputData().getInt(IDEXP,-1);
            String IdList = getInputData().getString(IDLIST);
            Log.d("El idExp es:",Integer.toString(IdExp));

            getSensedValues(IdExp, IdList);

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Llamada a Retrofit y a base de datos

        }


        //SystemClock.sleep(7000);



        return Result.SUCCESS;
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
        jsonObject.addProperty("idExp", "69");
        jsonObject.addProperty("duracion_min", "1");

        Api api = retrofit.create(Api.class);
        Call<List<SensedValuesContainer>> call = api.getSensedValues(jsonObject);

        call.enqueue(new Callback<List<SensedValuesContainer>>() {
            @Override
            public void onResponse(Call<List<SensedValuesContainer>> call, Response<List<SensedValuesContainer>> response) {
                List<SensedValuesContainer> values = response.body();

                for (SensedValuesContainer value : values) {
                    //{"id":"5","id_exp":"69","fechayhora":"2018-11-29 10:13:02","temp1":"-1000","temp2":"-1000","temp3":"-1000","temp4":"-1000","temp5":"-1000","tempPh":"-1000","tempAmb":"-10000","humity":"-1","pH":"-2"}
                   /* Log.d("id",value.getId());
                    Log.d("idExp",value.getId_exp());
                    Log.d("fechayhora",value.getFechayhora());

                    Log.d("Temp",value.getTemp1());*/

                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                //Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });

    }


    /*public void sendNotification(String title, String message) {
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
    }*/


}