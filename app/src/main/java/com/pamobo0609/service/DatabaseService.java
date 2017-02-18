package com.pamobo0609.service;

import android.app.IntentService;
import android.content.Intent;

import com.pamobo0609.constants.CodeChallengeConstants;
import com.pamobo0609.datasource.EarthquakeDataSource;
import com.pamobo0609.model.EarthquakeModel;

/**
 * Created by pamobo0609 on 2/18/17.
 */

public class DatabaseService extends IntentService {

    EarthquakeModel mModelToSave;

    EarthquakeDataSource mDataSource;

    public DatabaseService() {
        super("DatabaseService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mModelToSave = (EarthquakeModel) intent.getSerializableExtra(CodeChallengeConstants.EARTHQUAKES_KEY);
        if (null != mModelToSave) {
            mDataSource = new EarthquakeDataSource(this);
            mDataSource.open();
            mDataSource.saveEarthquakes(mModelToSave);
            mDataSource.close();
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mModelToSave = null;
        mDataSource = null;
    }
}
