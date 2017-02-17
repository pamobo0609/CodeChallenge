package com.pamobo0609.api;

import com.pamobo0609.model.EarthquakeModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by pamobo0609 on 2/17/17.
 */

public interface EarthquakeAPI {
    @GET("query")
    Call<EarthquakeModel> getEarthquakes(@Query("format") String format,
                                    @Query("starttime") String startTime, @Query("endtime") String endTime);
}
