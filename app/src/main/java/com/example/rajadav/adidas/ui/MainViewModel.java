package com.example.rajadav.adidas.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.rajadav.adidas.data.GoalsRepo;
import com.example.rajadav.adidas.model.CompletedGoal;
import com.example.rajadav.adidas.model.Goal;
import com.example.rajadav.adidas.model.SumPointsGoal;

import java.util.List;

//ViewModel to manage the list of goals, getting one goal by id, and others methods
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

    public void updateGoal(Goal goal){  goalRepo.updateGoal(goal);}

    public LiveData<SumPointsGoal> getPoints() {
        return goalRepo.getPoints();
    }

    public LiveData<SumPointsGoal> getDayPoints(int day, int month, int year) { return goalRepo.getDayPoints(day, month, year); }

    public LiveData<List<CompletedGoal>> getGoalsAchieved() {
        return goalRepo.getGoalsAchieved();
    }

    public void insertGoalCompletedIfNotExist(CompletedGoal goal){ goalRepo.insertGoalCompletedIfNotExist(goal); }
}
