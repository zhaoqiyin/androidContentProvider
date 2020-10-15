package com.example.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";

    public DBHelper(@Nullable Context context) {
        super(context, "person.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
        // 建表
        db.execSQL("create table person(_id integer primary key autoincrement, name vchar)");
        db.execSQL("insert into person (name)values('haha')");
        db.execSQL("insert into person (name)values('heihei')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
