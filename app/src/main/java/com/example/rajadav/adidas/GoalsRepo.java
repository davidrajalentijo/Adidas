package com.example.rajadav.adidas;

import android.app.Activity;
import android.arch.lifecycle.LiveData;

import com.example.rajadav.adidas.database.AppDatabase;
import com.example.rajadav.adidas.database.Goal;
import com.example.rajadav.adidas.database.GoalDao;
import com.example.rajadav.adidas.database.Items;
import com.example.rajadav.adidas.database.Webservice;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.FitnessOptions.Builder;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

public class GoalsRepo {

    public static final String BASE_URL = "https://thebigachallenge.appspot.com/_ah/api/myApi/v1/";
    private Webservice webservice;
    private GoalDao mGoalDao;
    private Executor executor;

    GoalsRepo(Context context) {
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webservice = retrofit.create(Webservice.class);
        AppDatabase db = AppDatabase.getInstance(context);
        mGoalDao = db.goalDao();
        executor = Executors.newSingleThreadExecutor();

    }

    public LiveData<List<Goal>> getGoals() {
        final LiveData<List<Goal>> data = mGoalDao.loadAllGoal();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<Items> response = webservice.getGoal().execute();
                    mGoalDao.insertGoals(response.body().getItems());
                } catch (IOException e) {
                    //TODO notify user of error
                }
            }
        });
        return data;
    }


}


