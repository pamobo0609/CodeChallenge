package com.pamobo0609.constants;

/**
 * Created by pamobo0609 on 2/17/17.
 */

public interface CodeChallengeConstants {
    /**
     * Base url for the request
     */
    String BASE_URL = "http://earthquake.usgs.gov/fdsnws/event/1/";

    String QUERY_FORMAT = "geojson";

    /**
     * Constants for extras
     */
    String LAT_KEY = "latitude";

    String LONG_KEY = "longitude";

    String EARTHQUAKES_KEY = "earthquakes";

    /**
     * Database constants.
     */
    String TABLE_NAME = "Earthquakes";

    String COLUMN_ID = "id";

    String COLUMN_PLACE = "place";

    String COLUMN_MAGNITUDE = "magnitude";

    String COLUMN_LAT = "latitude";

    String COLUMN_LONG = "longitude";

    /**
     * SQLite database name and version
     **/
    String DATABASE_NAME = "earthquakes";

    int DATABASE_VERSION = 1;

}
