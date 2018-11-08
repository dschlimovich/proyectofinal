package com.example.maceradores.maceracion.retrofitInterface;

import com.example.maceradores.maceracion.RetrofitGsonContainer.TempPh;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    String BASE_URL = "http://192.168.1.11/pruebaapi/";


    @GET("apiGetTempPh.php")
    Call<TempPh> getTempPh();
}
