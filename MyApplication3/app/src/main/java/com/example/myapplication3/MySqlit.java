package com.example.myapplication3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * @创建时间 2020/10/16 10:15
 */
public class MySqlit extends SQLiteOpenHelper {
    public MySqlit(@Nullable Context context) {
        super(context, "my.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table demo(id integer primary key autoincrement,disc text,title,text,image text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
