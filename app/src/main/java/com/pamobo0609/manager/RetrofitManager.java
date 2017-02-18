package com.pamobo0609.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pamobo0609.api.EarthquakeAPI;
import com.pamobo0609.constants.CodeChallengeConstants;
import com.pamobo0609.model.EarthquakeModel;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pamobo0609 on 2/17/17.
 */

public class RetrofitManager {

    private static RetrofitManager instance;

    private EarthquakeAPI mConsumer;

    private RetrofitManager() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(CodeChallengeConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mConsumer = retrofit.create(EarthquakeAPI.class);
    }

    public static RetrofitManager getInstance() {
        if (null == instance) {
            instance = new RetrofitManager();
        }
        return instance;
    }


    public void getEarthquakes(String pFormat, String pStartTime, String pEndTime,
                               final OnGetEarthquakesResponse pListener) {

        final Call<EarthquakeModel> call = mConsumer.getEarthquakes( pFormat, pStartTime,
                pEndTime);

        call.enqueue(new Callback<EarthquakeModel>() {
            @Override
            public void onResponse(Call<EarthquakeModel> call, Response<EarthquakeModel> response) {

                response.body();

                if (response.isSuccessful()) {
                    pListener.onSuccess(response.body());
                } else {
                    pListener.onFailure();
                }
            }

            @Override
            public void onFailure(Call<EarthquakeModel> call, Throwable t) {
                t.getMessage();
                pListener.onFailure();
            }
        });

    }

    public interface OnGetEarthquakesResponse {
        void onSuccess(EarthquakeModel pModel);

        void onFailure();
    }
}