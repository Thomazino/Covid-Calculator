package com.example.helloapp;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME="CovidStats.db";
    public DatabaseOpenHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }
}
