package com.pamobo0609.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pamobo0609.constants.CodeChallengeConstants;

/**
 * Created by pamobo0609 on 2/18/17.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_INIT = "create table "+CodeChallengeConstants.TABLE_NAME+"(id integer primary key autoincrement," +
            CodeChallengeConstants.COLUMN_PLACE+" text not null, "+CodeChallengeConstants.COLUMN_MAGNITUDE
            +" numeric not null, "+CodeChallengeConstants.COLUMN_LAT+" numeric not null, "+CodeChallengeConstants.COLUMN_LONG+" numeric not null);";


    public SQLiteHelper(Context context) {
        super(context, CodeChallengeConstants.DATABASE_NAME, null, CodeChallengeConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_INIT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
