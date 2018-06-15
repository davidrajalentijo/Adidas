package com.example.rajadav.adidas.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface GoalDao {
    @Query("SELECT * FROM goal")
    LiveData<List<Goal>> loadAllGoal();
}
