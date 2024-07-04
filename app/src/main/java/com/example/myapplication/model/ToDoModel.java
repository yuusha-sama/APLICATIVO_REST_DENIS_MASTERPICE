package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class ToDoModel {

    @SerializedName("id_task")
    private int idTask;

    @SerializedName("task")
    private String task;

    @SerializedName("taskStatus")
    private int taskStatus; // Alterado para int

    // Getters and setters
    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }
}
