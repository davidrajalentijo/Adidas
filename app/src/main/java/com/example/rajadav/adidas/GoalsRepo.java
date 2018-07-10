package com.example.rajadav.adidas;

import android.arch.lifecycle.LiveData;

import com.example.rajadav.adidas.database.AppDatabase;
import com.example.rajadav.adidas.database.Goal;
import com.example.rajadav.adidas.database.GoalDao;
import com.example.rajadav.adidas.database.Items;
import com.example.rajadav.adidas.database.Webservice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;


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

    public LiveData<Goal> getGoalDetail(int goalid) {
        return mGoalDao.getOneGoal(goalid);
    }

}


