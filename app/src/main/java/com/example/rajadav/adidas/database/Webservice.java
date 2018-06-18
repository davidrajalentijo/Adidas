package com.example.rajadav.adidas.database;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import java.util.List;
import android.util.Log;

public interface Webservice {

    @GET("goals")
    Call<Items>getGoal();
}