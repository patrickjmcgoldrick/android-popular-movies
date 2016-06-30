package com.mcgoldricksolutions.udacity.nanodegree.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by dirtbag on 6/30/16.
 */
public class TestDb  extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(FavoriteDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }


    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(FavoriteContract.FavoriteEntry.TABLE_NAME);

        mContext.deleteDatabase(FavoriteDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new FavoriteDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + FavoriteContract.FavoriteEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> favoriteColumnHashSet = new HashSet<String>();
        favoriteColumnHashSet.add(FavoriteContract.FavoriteEntry._ID);
        favoriteColumnHashSet.add(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID);
        favoriteColumnHashSet.add(FavoriteContract.FavoriteEntry.COLUMN_POSTER_URL);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            favoriteColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                favoriteColumnHashSet.isEmpty());
        db.close();
    }

    /**
     * Test insert and retreval of data.
     */
    public void testFavoriteTable() {
        // First step: Get reference to writable database
        mContext.deleteDatabase(FavoriteDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new FavoriteDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)
        ContentValues contentValues = TestUtilities.createFavoriteValues();

        // Insert ContentValues into database and get a row ID back
        long row = db.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, contentValues);
        assertFalse("Insert of ContentValues failed, returned -1", row == -1);

        // Query the database and receive a Cursor back
        Cursor cursor = db.query(FavoriteContract.FavoriteEntry.TABLE_NAME,
                null,
                null, null,
                null, null,
                null);
        assertTrue("We do not have exactly 1 row", cursor.getCount() == 1);

        // Move the cursor to a valid database row
        cursor.moveToFirst();

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("failed to validate data inserted with data in database.",
                cursor,
                contentValues);

        // Finally, close the cursor and database
        db.close();
    }

}
