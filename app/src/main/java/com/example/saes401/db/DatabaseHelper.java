package com.example.saes401.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "gameDataBase.db";
    private static final int DATABASE_VERSON = 1;
    private static final String TABLE_NAME = "playerGame";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_DURATION = "duration";
    private static final String CREATE_TABLE_PLAYER_DATA =
            "CREATE TABLE " + TABLE_NAME + " ("+
                    COLUMN_ID + "INTERGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SCORE + "INTEGER, " +
                    COLUMN_DATE + "TEXT, " +
                    COLUMN_SCORE + "INTEGER" +
                    COLUMN_DURATION + "INTEGER" + //en seconde
                    ");";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSON);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PLAYER_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
