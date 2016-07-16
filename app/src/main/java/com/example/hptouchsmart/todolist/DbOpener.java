package com.example.hptouchsmart.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hptouchsmart.todolist.db.TaskTable;

/**
 * Created by hp TouchSmart on 7/13/2016.
 */
public class DbOpener extends SQLiteOpenHelper {

    public static final String DB_NAME = "taskDatabase";
    public static final int DB_VERSION  = 1;
    public static DbOpener dbOpener = null;

    public static SQLiteDatabase openReadableData(Context c){

            if (dbOpener == null){
                dbOpener = new DbOpener(c);
        }
        return  dbOpener.getReadableDatabase();

    }


    public static SQLiteDatabase openWritableData(Context c){

        if (dbOpener == null){
            dbOpener = new DbOpener(c);
        }

        return dbOpener.getWritableDatabase();
    }



    public DbOpener(Context context) {
        super(context, DB_NAME , null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TaskTable.TABLE_CREATE_CMD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
