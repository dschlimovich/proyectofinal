package com.example.maceradores.maceracion.retrofitInterface;

import com.example.maceradores.maceracion.RetrofitGsonContainer.SensedValuesContainer;
import com.example.maceradores.maceracion.RetrofitGsonContainer.TempPh;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {
    String BASE_URL = "http://192.168.1.11/pruebaapi/";


    @GET("apiGetTempPh.php")
    Call<TempPh> getTempPh();

    @POST("apiEscribe.php")
    Call<Void> postExperiment(@Body JsonObject NewExperiment);

    @POST("apiGet.php")
    Call<List<SensedValuesContainer>> getSensedValues(@Body JsonObject jsonObject);

    @POST("apiCancel.php")
    Call<Void> cancelExperiment(@Body JsonObject idExperiment);
}
