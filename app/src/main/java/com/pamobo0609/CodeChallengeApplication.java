package com.pamobo0609;

import android.app.Application;

import com.pamobo0609.datasource.EarthquakeDataSource;

/**
 * Created by pamobo0609 on 2/18/17.
 */

public class CodeChallengeApplication extends Application {

    EarthquakeDataSource mDataSource;

    @Override
    public void onCreate() {
        super.onCreate();

        // If the database is already created, the code doesn't run
        if (!EarthquakeDataSource.databaseExists()) {
            mDataSource = new EarthquakeDataSource(this);
            mDataSource.open();
        }
    }


}
