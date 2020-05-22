package com.example.helloapp;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME="Stats.db";
    public DatabaseOpenHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }
}
