package com.developer.dschlimovich.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    //String BASE_URL = "http://192.168.1.11/pruebaapi/apiGetTempPh.php";
    String BASE_URL = "http://192.168.1.11/pruebaapi/";

    //@GET
    @GET("apiGetTempPh.php")
    Call<TempPh> getTempPh();


}
