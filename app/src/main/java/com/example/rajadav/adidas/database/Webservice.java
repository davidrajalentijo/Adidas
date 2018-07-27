package com.example.rajadav.adidas.database;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Webservice {

    @GET("goals")
    Call<Items> getGoal();

}