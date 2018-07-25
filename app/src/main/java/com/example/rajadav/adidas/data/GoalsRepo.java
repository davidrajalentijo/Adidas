package com.example.rajadav.adidas.data;

import android.arch.lifecycle.LiveData;

import com.example.rajadav.adidas.database.AppDatabase;
import com.example.rajadav.adidas.model.CompletedGoal;
import com.example.rajadav.adidas.model.Goal;
import com.example.rajadav.adidas.database.GoalDao;
import com.example.rajadav.adidas.model.Items;
import com.example.rajadav.adidas.model.SumPointsGoal;
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

//This method implements retrofit to get the goals data
public class GoalsRepo {

    public static final String BASE_URL = "https://thebigachallenge.appspot.com/_ah/api/myApi/v1/";
    private Webservice webservice;
    private GoalDao mGoalDao;
    private Executor executor;

    public GoalsRepo(Context context) {
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

    /*
     This method return the list of all Goals
    */
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

    /*
    This method return one goal by a given value
     */
    public LiveData<Goal> getGoalDetail(int goalid) {
        return mGoalDao.getOneGoal(goalid);
    }

    /*
This method update a goal
 */
    public void updateGoal(Goal goal) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mGoalDao.updateGoal(goal);
            }
        });
    }

    /*
 This method return the total of Points achieved
*/
    public LiveData<SumPointsGoal> getPoints() {
        return mGoalDao.loadAllPointsAchieved();
    }

    /*
 This method return the total of Points achieved today
*/
    public LiveData<SumPointsGoal> getDayPoints(int day, int month, int year) {
        return mGoalDao.getDayPoints(day,month, year);
    }

    /*
 This method return the list of all Goals achieved order by date
*/
    public LiveData<List<CompletedGoal>> getGoalsAchieved() {
        final LiveData<List<CompletedGoal>> data = mGoalDao.loadAllGoalsAchieved();
        return data;
    }

    /*
This method insert a goal if its completed
*/
    public void insertGoalCompletedIfNotExist(CompletedGoal goal) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mGoalDao.insertGoalCompletedIfNotExist(goal);
            }
        });
    }

}


