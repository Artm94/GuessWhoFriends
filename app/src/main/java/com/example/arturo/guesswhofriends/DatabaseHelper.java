package com.example.arturo.guesswhofriends;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by arturo on 26/11/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "MovieTrivia.db";
    public static int DATABASE_VERSION = 4;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    public static class HighScoresTable implements BaseColumns{
        public static final String TABLE_NAME = "highscores";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_SCORE = "score";
    }

    public static final String SQL_CREATE_HIGHSCORES =
            "CREATE TABLE " + HighScoresTable.TABLE_NAME + " (" +
                    HighScoresTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY,"  +
                    HighScoresTable.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    HighScoresTable.COLUMN_NAME_SCORE + " INTEGER" + ")";

    public static final String SQL_DELETE_HIGHSCORES =
            "DROP TABLE IF EXISTS " + HighScoresTable.TABLE_NAME;


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_HIGHSCORES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_HIGHSCORES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public ArrayList<HighScore> getHighScores(int quantity){
        String[] columns = {HighScoresTable.COLUMN_NAME_ID, HighScoresTable.COLUMN_NAME_DATE, HighScoresTable.COLUMN_NAME_SCORE};
        String order = HighScoresTable.COLUMN_NAME_SCORE + " DESC";
        String limit = String.valueOf(quantity);
        Cursor cursor = getReadableDatabase().query(HighScoresTable.TABLE_NAME, columns, null,
                null, null, null, order, limit);
        ArrayList<HighScore> highScores = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(HighScoresTable.COLUMN_NAME_ID));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(HighScoresTable.COLUMN_NAME_DATE));
                int score = cursor.getInt(cursor.getColumnIndexOrThrow(HighScoresTable.COLUMN_NAME_SCORE));
                highScores.add(new HighScore(id, date, score));
            }while (cursor.moveToNext());
        }
        return highScores;
    }

    public boolean insertHighScore(HighScore highScore){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HighScoresTable.COLUMN_NAME_DATE, highScore.getDate());
        contentValues.put(HighScoresTable.COLUMN_NAME_SCORE, highScore.getScore());
        long id = db.insert(HighScoresTable.TABLE_NAME, null, contentValues);
        return id != -1;
    }
}
