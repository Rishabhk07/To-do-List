package com.example.hptouchsmart.todolist.db;

/**
 * Created by hp TouchSmart on 7/13/2016.
 */
public class TaskTable {


    public static final String TABLE_NAME  = "tasks_update";

    public interface Colums{
        String ID = "id";
        String TASK = "task";
        String DONE = "done";
        String DATE = "date";

    }


    public static final String TABLE_CREATE_CMD =

            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                    + DbTable.LBR
                    + Colums.ID + DbTable.TYPE_INT_PK + DbTable.COMMA
                    + Colums.TASK + DbTable.TYPE_TEXT + DbTable.COMMA
                    + Colums.DONE + DbTable.TYPE_INTEGER + "DEFAULT 0" + DbTable.COMMA
                    + Colums.DATE + DbTable.TYPE_TEXT
                    +DbTable.RBR + ";" ;

}
