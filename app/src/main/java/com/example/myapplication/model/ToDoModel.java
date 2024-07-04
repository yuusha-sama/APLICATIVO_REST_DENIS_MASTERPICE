package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class ToDoModel {

    @SerializedName("id")
    private int id;

    @SerializedName("task")
    private String task;

    @SerializedName("status")
    private int status;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
