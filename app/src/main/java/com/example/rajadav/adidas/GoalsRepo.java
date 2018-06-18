package com.example.rajadav.adidas;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.rajadav.adidas.database.Goal;
import com.example.rajadav.adidas.database.Items;
import com.example.rajadav.adidas.database.Webservice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoalsRepo {

    public static final String BASE_URL = "https://thebigachallenge.appspot.com/_ah/api/myApi/v1/";
    private Webservice webservice;
    final MutableLiveData<List<Goal>> goals = new MutableLiveData<>();

    public LiveData<List<Goal>> getGoals() {
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Webservice service = retrofit.create(Webservice.class);

        final MutableLiveData<List<Goal>> data = new MutableLiveData<>();
        service.getGoal().enqueue(new Callback<Items>() {
            @Override
            public void onResponse(Call<Items> call, Response<Items> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body().getItems());
                }else {
                    //TODO notifiy error to the user somehow
                }
            }
            @Override
            public void onFailure(Call<Items> call, Throwable t) {
                //TODO Throw error
            }
        });

        return data;
    }
}


