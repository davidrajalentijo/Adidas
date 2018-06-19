package com.example.rajadav.adidas.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
@Entity(tableName = "goal")
public class Goal {
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

    public Goal(int id, String title, String description, String type, Integer goal) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.goal = goal;
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
}
