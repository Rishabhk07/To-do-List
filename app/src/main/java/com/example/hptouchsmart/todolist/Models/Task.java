package com.example.hptouchsmart.todolist.Models;

/**
 * Created by hp TouchSmart on 7/13/2016.
 */
public class Task {

    int id;
    String task;
    int done;
    String date;

    public Task(String task,int done,String date) {
        this.done = done;
        this.task = task;
        this.date = date;
    }

    public Task( int id ,String task, int done , String date) {
        this.id = id;
        this.task = task;
        this.done = done;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public void setTask(String task) {
        this.task = task;
    }


}
