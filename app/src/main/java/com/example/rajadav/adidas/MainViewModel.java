package com.example.rajadav.adidas;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.rajadav.adidas.database.Goal;

import java.util.List;

public class MainViewModel extends ViewModel {

    private GoalsRepo goalRepo;

    public MainViewModel(Context context) {
        goalRepo = new GoalsRepo(context);
    }

    public LiveData<List<Goal>> getGoals() {
        return goalRepo.getGoals();
    }

    public LiveData<Goal> getGoalDetail(int id) {
        return goalRepo.getGoalDetail(id);
    }
}
