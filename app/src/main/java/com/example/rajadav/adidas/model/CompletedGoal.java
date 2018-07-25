package com.example.rajadav.adidas.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.rajadav.adidas.database.DateConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "completedgoal")
@TypeConverters({DateConverter.class})
public class CompletedGoal {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    private int id;

    @ColumnInfo(name = "goalid")
    @SerializedName("goalid")
    @Expose
    private int goalid;

    @ColumnInfo(name = "day")
    @SerializedName("day")
    @Expose
    private int day;

    @ColumnInfo(name = "month")
    @SerializedName("month")
    @Expose
    private int month;

    @ColumnInfo(name = "year")
    @SerializedName("year")
    @Expose
    private int year;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("trophy")
    @Expose
    private String trophy;

    @SerializedName("points")
    @Expose
    private Integer points;

    @ColumnInfo(name = "hour")
    @SerializedName("hour")
    @Expose
    private int hour;

    @ColumnInfo(name = "minutes")
    @SerializedName("minutes")
    @Expose
    private int minutes;

    @ColumnInfo(name = "seconds")
    @SerializedName("seconds")
    @Expose
    private int seconds ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoalid() {
        return goalid;
    }

    public void setGoalid(int goalid) {
        this.goalid = goalid;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDay() { return day; }

    public void setDay(int day) { this.day = day; }

    public int getMonth() { return month; }

    public void setMonth(int month) { this.month = month; }

    public int getYear() { return year; }

    public void setYear(int year) { this.year = year; }

    public int getHour() { return hour; }

    public void setHour(int hour) { this.hour = hour; }

    public int getMinutes() { return minutes; }

    public void setMinutes(int minutes) { this.minutes = minutes; }

    public int getSeconds() { return seconds; }

    public void setSeconds(int seconds) { this.seconds = seconds; }

}
