package com.pamobo0609.api;

import com.pamobo0609.model.EarthquakeModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by pamobo0609 on 2/17/17.
 */

public interface EarthquakeAPI {
    /**
     * <h1>GetEarthquakes</h1>
     * <p>API method to get the {@link EarthquakeModel} that contains all earthquakes.</p>
     * @param format the format for the request response
     * @param startTime the start date for the date range
     * @param endTime the end date for the date range
     * @return a {@link Call<EarthquakeModel>} object
     */
    @GET("query")
    Call<EarthquakeModel> getEarthquakes(@Query("format") String format,
                                    @Query("starttime") String startTime, @Query("endtime") String endTime);
}
