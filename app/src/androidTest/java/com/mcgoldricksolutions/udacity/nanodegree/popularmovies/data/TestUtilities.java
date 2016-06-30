package com.mcgoldricksolutions.udacity.nanodegree.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

/**
 * Created by dirtbag on 6/30/16.
 */
public class TestUtilities extends AndroidTestCase {

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    // create default favorite record.
    static ContentValues createFavoriteValues() {
        ContentValues favoriteValues = new ContentValues();

        // Captain America: Civil War
        favoriteValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID, 271110);
        favoriteValues.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER_URL, "/2tOgiY533JSFp7OrVlkeRJvsZpI.jpg");

        return favoriteValues;
    }
}
