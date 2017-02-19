package com.pamobo0609;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.pamobo0609.datasource.EarthquakeDataSource;

/**
 * Created by pamobo0609 on 2/18/17.
 */

public class CodeChallengeApplication extends MultiDexApplication {

    EarthquakeDataSource mDataSource;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

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
