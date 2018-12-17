package com.developer.dschlimovich.retrofit;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mLoadingIndicator;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.tv_show_data);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        new FetchtTempPhTask().execute();
    }

    private void showProgressBar(){
        mTextView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showText(){
        mTextView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    public class FetchtTempPhTask extends AsyncTask<String, Void, String[]> {

        // COMPLETED (18) Within your AsyncTask, override the method onPreExecute and show the loading indicator
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {
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
                    String temperature = tempPh.getTemp1();
                    mTextView.setText("La temperatura es: " + temperature);
                }
                @Override
                public void onFailure(Call<TempPh> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String[] weatherData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (weatherData != null) {
                showText();
            }
        }
    }




}
