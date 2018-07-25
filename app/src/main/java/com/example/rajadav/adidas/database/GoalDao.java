package com.example.rajadav.adidas.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.example.rajadav.adidas.model.CompletedGoal;
import com.example.rajadav.adidas.model.Goal;
import com.example.rajadav.adidas.model.SumPointsGoal;

import java.util.Date;
import java.util.List;

@Dao
public abstract class GoalDao {

    @Query("SELECT * FROM goal")
    public abstract LiveData<List<Goal>> loadAllGoal();

    @Query("SELECT * FROM goal where id=:id")
    public abstract LiveData<Goal> getOneGoal(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertGoals(List<Goal> goal);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void updateGoal(Goal goal);

    @Query("SELECT SUM(points) as totalpoints FROM CompletedGoal")
    public abstract LiveData<SumPointsGoal> loadAllPointsAchieved();

    @Query("SELECT SUM(points) as totalpoints FROM CompletedGoal where day=:day AND month=:month AND year=:year")
    public abstract LiveData<SumPointsGoal> getDayPoints(int day, int month, int year);

    @Query("SELECT * FROM CompletedGoal ORDER BY day DESC, month DESC, year DESC , hour DESC, minutes DESC, seconds DESC")
    public abstract LiveData<List<CompletedGoal>> loadAllGoalsAchieved();

    @Query("SELECT * FROM CompletedGoal where day=:day AND month=:month AND year=:year AND goalid=:goalid LIMIT 1")
    public abstract LiveData<CompletedGoal> checkIfGoalIsDone(int day, int month, int year, int goalid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertGoalCompleted(CompletedGoal goal);

    @Query("SELECT * FROM CompletedGoal where day=:day AND month=:month AND year=:year AND goalid=:goalid LIMIT 1")
    public abstract CompletedGoal goalIsDone(int day, int month, int year, int goalid);

    @Transaction
    public void insertGoalCompletedIfNotExist(CompletedGoal goal) {

        CompletedGoal newgoal = goalIsDone(goal.getDay(), goal.getMonth(), goal.getYear(), goal.getGoalid());

            if (newgoal == null){
                CompletedGoal newCompleted = new CompletedGoal();
                newCompleted.setGoalid(goal.getGoalid());
                newCompleted.setDay(goal.getDay());
                newCompleted.setMonth(goal.getMonth());
                newCompleted.setYear(goal.getYear());
                newCompleted.setTitle(goal.getTitle());
                newCompleted.setPoints(goal.getPoints());
                newCompleted.setTrophy(goal.getTrophy());
                newCompleted.setHour(goal.getHour());
                newCompleted.setMinutes(goal.getMinutes());
                newCompleted.setSeconds(goal.getSeconds());
                insertGoalCompleted(newCompleted);
            }
    }

}
