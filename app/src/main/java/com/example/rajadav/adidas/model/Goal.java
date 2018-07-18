package com.example.rajadav.adidas.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

@Entity(tableName = "goal")
public class Goal {
    public static final String BRONZE_REWARD = "bronze_medal";
    public static final String SILVER_REWARD = "silver_medal";
    public static final String GOLD_REWARD = "gold_medal";
    public static final String ZOMBIE_REWARD = "zombie_hand";

    @PrimaryKey()
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    private int id;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    @Expose
    private String description;

    @ColumnInfo(name = "type")
    @SerializedName("type")
    @Expose
    private String type;

    @ColumnInfo(name = "goal")
    @SerializedName("goal")
    @Expose
    private Integer goal;

    @ColumnInfo(name = "steps")
    @SerializedName("steps")
    @Expose
    private int steps;

    @ColumnInfo(name = "distance")
    @SerializedName("distance")
    @Expose
    private int distance;

    @Embedded
    private Reward reward;

    public Goal(int id, String title, String description, String type, Integer goal) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.goal = goal;
        this.steps = 0;
        distance = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}
