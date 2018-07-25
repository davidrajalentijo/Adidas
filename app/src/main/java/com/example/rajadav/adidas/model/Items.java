package com.example.rajadav.adidas.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import java.util.List;

public class Items {

    @SerializedName("items")
    @Expose
    private List<Goal> goals = null;

    @SerializedName("itemsdone")
    @Expose
    private List<CompletedGoal> goalsdone = null;

    @SerializedName("nextPageToken")
    @Expose
    private String nextPageToken;

    public List<Goal> getItems() {
        return goals;
    }

    public void setItems(List<Goal> goals) {
        this.goals = goals;
    }

    public List<CompletedGoal> getGoalsdone() {
        return goalsdone;
    }

    public void setGoalsdone(List<CompletedGoal> goalsdone) {
        this.goalsdone = goalsdone;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
}
