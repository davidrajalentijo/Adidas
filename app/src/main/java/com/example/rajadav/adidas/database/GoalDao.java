package com.example.rajadav.adidas.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.rajadav.adidas.model.Goal;

import java.util.List;

@Dao
public interface GoalDao {

    @Query("SELECT * FROM goal")
    LiveData<List<Goal>> loadAllGoal();

    @Query("SELECT * FROM goal where id=:id")
    LiveData<Goal> getOneGoal(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGoals(List<Goal> goal);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    //@Query("UPDATE goal SET steps=:steps WHERE id = :id")
    void updateGoal(Goal goal);
/*
    @Query("SELECT SUM(points) FROM goal where datereward=:datereward")
    LiveData<List<Goal>> loadAllPointsAchieved();

    @Query("SELECT * FROM goal ORDER BY datereward DESC")
    LiveData<List<Goal>> loadAllGoalsAchieved();*/
}
