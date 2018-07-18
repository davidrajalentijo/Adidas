package com.example.rajadav.adidas.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class Reward {

    @SerializedName("trophy")
    @Expose
    private String trophy;

    @SerializedName("points")
    @Expose
    private Integer points;

    public String getTrophy() {
        return trophy;
    }

    public void setTrophy(String trophy) {
        this.trophy = trophy;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

}

