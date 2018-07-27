package com.example.rajadav.adidas.data;

import com.example.rajadav.adidas.model.Items;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Webservice {

    @GET("goals")
    Call<Items> getGoal();

}