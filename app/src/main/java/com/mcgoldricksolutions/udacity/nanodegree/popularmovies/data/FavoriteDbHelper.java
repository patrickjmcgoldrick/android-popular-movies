package com.mcgoldricksolutions.udacity.nanodegree.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mcgoldricksolutions.udacity.nanodegree.popularmovies.data.FavoriteContract.FavoriteEntry;

/**
 * Created by dirtbag on 6/30/16.
 */
public class FavoriteDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";


    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteEntry.TABLE_NAME + "(" +

                FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                // the ID of the movie from API
                FavoriteEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavoriteEntry.COLUMN_POSTER_URL + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_CATEGORY + " INTEGER NOT NULL, " +

                // Avoid duplicates
                " UNIQUE (" + FavoriteEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // On upgrade just discard previous data and rebuild table structure.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
