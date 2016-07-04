package com.mcgoldricksolutions.udacity.nanodegree.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by dirtbag on 6/30/16.
 */
public class FavoriteProvider extends ContentProvider {

        // The URI Matcher used by this content provider.
        private static final UriMatcher sUriMatcher = buildUriMatcher();
        private FavoriteDbHelper mOpenHelper;

        static final int FAVORITE  = 100;
        static final int FAVORITES = 101;

        //private static final SQLiteQueryBuilder sWeatherByLocationSettingQueryBuilder;





        /*
            Students: Here is where you need to create the UriMatcher. This UriMatcher will
            match each URI to the WEATHER, WEATHER_WITH_LOCATION, WEATHER_WITH_LOCATION_AND_DATE,
            and LOCATION integer constants defined above.  You can test this by uncommenting the
            testUriMatcher test within TestUriMatcher.
         */
        static UriMatcher buildUriMatcher() {
            // 1) The code passed into the constructor represents the code to return for the root
            // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.


            // 2) Use the addURI function to match each of the types.  Use the constants from
            // WeatherContract to help define the types to the UriMatcher.


            // 3) Return the new matcher!
            return null;
        }

        /*
            Students: We've coded this for you.  We just create a new WeatherDbHelper for later use
            here.
         */
        @Override
        public boolean onCreate() {
            mOpenHelper = new FavoriteDbHelper(getContext());
            return true;
        }

        /*
            Students: Here's where you'll code the getType function that uses the UriMatcher.  You can
            test this by uncommenting testGetType in TestProvider.

         */
        @Override
        public String getType(Uri uri) {

            // Use the Uri Matcher to determine what kind of URI this is.
            final int match = sUriMatcher.match(uri);

            switch (match) {
                 case FAVORITE:
                    return FavoriteContract.FavoriteEntry.CONTENT_TYPE;
                case FAVORITES:
                    return FavoriteContract.FavoriteEntry.CONTENT_ITEM_TYPE;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        @Override
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                            String sortOrder) {

            final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
            // Query the database for all Favorite records
            Cursor retCursor = db.query(FavoriteContract.FavoriteEntry.TABLE_NAME,
                    null,
                    null, null,
                    null, null,
                    null);

            //retCursor.setNotificationUri(getContext().getContentResolver(), uri);
            return retCursor;
        }

        /*
            Student: Add the ability to insert Locations to the implementation of this function.
         */
        @Override
        public Uri insert(Uri uri, ContentValues values) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            final int match = sUriMatcher.match(uri);
            Uri returnUri;

            switch (match) {
                case FAVORITE: {
                    long _id = db.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, values);
                    if ( _id > 0 )
                        returnUri = FavoriteContract.FavoriteEntry.buildFavoriteUri(_id);  //.buildWeatherUri(_id);
                    else
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    break;
                }
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return returnUri;
        }

        @Override
        public int delete(Uri uri, String selection, String[] selectionArgs) {
            // Student: Start by getting a writable database

            // Student: Use the uriMatcher to match the WEATHER and LOCATION URI's we are going to
            // handle.  If it doesn't match these, throw an UnsupportedOperationException.

            // Student: A null value deletes all rows.  In my implementation of this, I only notified
            // the uri listeners (using the content resolver) if the rowsDeleted != 0 or the selection
            // is null.
            // Oh, and you should notify the listeners here.

            // Student: return the actual rows deleted
            return 0;
        }

        @Override
        public int update(
                Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            // Student: This is a lot like the delete function.  We return the number of rows impacted
            // by the update.
            return 0;
        }

//        @Override
//        public int bulkInsert(Uri uri, ContentValues[] values) {
//            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//            final int match = sUriMatcher.match(uri);
//            switch (match) {
//                case WEATHER:
//                    db.beginTransaction();
//                    int returnCount = 0;
//                    try {
//                        for (ContentValues value : values) {
//                            normalizeDate(value);
//                            long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, value);
//                            if (_id != -1) {
//                                returnCount++;
//                            }
//                        }
//                        db.setTransactionSuccessful();
//                    } finally {
//                        db.endTransaction();
//                    }
//                    getContext().getContentResolver().notifyChange(uri, null);
//                    return returnCount;
//                default:
//                    return super.bulkInsert(uri, values);
//            }
//        }

        // You do not need to call this method. This is a method specifically to assist the testing
        // framework in running smoothly. You can read more at:
        // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
        @Override
        @TargetApi(11)
        public void shutdown() {
            mOpenHelper.close();
            super.shutdown();
        }
    }
