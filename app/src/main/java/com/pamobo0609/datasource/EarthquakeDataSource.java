package com.pamobo0609.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.pamobo0609.constants.CodeChallengeConstants;
import com.pamobo0609.helper.SQLiteHelper;
import com.pamobo0609.model.EarthquakeModel;
import com.pamobo0609.model.Feature;
import com.pamobo0609.model.Geometry;
import com.pamobo0609.model.Properties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pamobo0609 on 2/18/17.
 */

public class EarthquakeDataSource {

    private static final String TRUNCATE_TABLE = "DELETE FROM ";

    private SQLiteDatabase mDatabase;

    private SQLiteHelper mHelper;

    private static final String[] ALL_COLUMNS = {CodeChallengeConstants.COLUMN_ID, CodeChallengeConstants.COLUMN_PLACE,
            CodeChallengeConstants.COLUMN_MAGNITUDE, CodeChallengeConstants.COLUMN_LAT, CodeChallengeConstants.COLUMN_LONG};

    /**
     * <h1>EarthquakeDataSource</h1>
     * <p>Creates a instance of the {@link EarthquakeDataSource} class.</p>
     *
     * @param pRelatedContext the related {@link Context}
     */
    public EarthquakeDataSource(Context pRelatedContext) {
        mHelper = new SQLiteHelper(pRelatedContext);
    }

    /**
     * <h1>open</h1>
     * <p>Opens the database connection</p>
     *
     * @throws SQLException
     */
    public synchronized void open() throws SQLException {
        mDatabase = mHelper.getWritableDatabase();
    }

    public synchronized void close() {
        mHelper.close();
    }

    /**
     * <h1>databaseExists</h1>
     * <p>Check if the database exist and can be read.</p>
     *
     * @return true if it exists and can be read, false if it doesn't
     */
    public static boolean databaseExists() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase("/data/user/0/com.pamobo0609/databases/"+
                    CodeChallengeConstants.DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null;
    }

    /***
     * <h1>CursorToFeature</h1>
     * <p>Converts from {@link Cursor} to {@link Feature}</p>
     *
     * @param cursor {@link Cursor} to iterate in the database
     * @return a {@link Feature} object
     */
    private Feature cursorToFeature(Cursor cursor) {
        Feature feature = new Feature();

        // Getting the properties
        Properties properties = new Properties();
        properties.setPlace(cursor.getString(1));
        properties.setMag(cursor.getDouble(2));

        // Getting the coordinates
        Geometry geometry = new Geometry();
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(cursor.getDouble(3));
        coordinates.add(cursor.getDouble(4));
        geometry.setCoordinates(coordinates);

        feature.setProperties(properties);
        feature.setGeometry(geometry);

        return feature;
    }

    /**
     * <h1>saveEarthquakes</h1>
     * <p>Saves the last searched quakes into the database.</p>
     *
     * @param pToSave the {@link EarthquakeModel} to save in the database
     */
    public synchronized void saveEarthquakes(EarthquakeModel pToSave) {
        // We truncate the table, to only save the last search
        mDatabase.execSQL(TRUNCATE_TABLE+CodeChallengeConstants.TABLE_NAME);

        ContentValues contentToSave = new ContentValues();

        List<Feature> listToSave = pToSave.getFeatures();

        Cursor cursor = null;

        for (int i = 0; i < listToSave.size(); i++) {
            Feature feature = listToSave.get(i);
            contentToSave.put(CodeChallengeConstants.COLUMN_PLACE, feature.getProperties().getPlace());
            contentToSave.put(CodeChallengeConstants.COLUMN_MAGNITUDE, feature.getProperties().getMag());
            contentToSave.put(CodeChallengeConstants.COLUMN_LAT, feature.getGeometry().getCoordinates().get(0));
            contentToSave.put(CodeChallengeConstants.COLUMN_LONG, feature.getGeometry().getCoordinates().get(1));

            long insertId = mDatabase.insert(CodeChallengeConstants.TABLE_NAME, null,
                    contentToSave);

            cursor = mDatabase.query(CodeChallengeConstants.TABLE_NAME,
                    ALL_COLUMNS, CodeChallengeConstants.COLUMN_ID + " = " + insertId, null,
                    null, null, null);
        }

        if (null != cursor) {
            cursor.close();
        }
    }

    /**
     * <h1>getEarthquakes</h1>
     * <p>Returns the saved earthquakes</p>
     *
     * @return a {@link EarthquakeModel} object
     */
    public EarthquakeModel getEarthquakes() {
        Cursor cursor = mDatabase.query(CodeChallengeConstants.TABLE_NAME, ALL_COLUMNS, null, null, null, null, null);
        cursor.moveToFirst();

        ArrayList<Feature> features = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            Feature f = cursorToFeature(cursor);
            features.add(f);
            cursor.moveToNext();
        }
        cursor.close();

        EarthquakeModel quakes = new EarthquakeModel(features);

        return quakes;
    }
}
