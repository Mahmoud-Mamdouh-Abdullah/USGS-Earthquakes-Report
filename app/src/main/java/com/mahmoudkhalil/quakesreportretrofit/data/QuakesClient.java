package com.mahmoudkhalil.quakesreportretrofit.data;

import com.google.gson.JsonObject;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuakesClient {
    private static final String BASE_URL = "https://earthquake.usgs.gov/fdsnws/event/1/";
    private UsgsApi usgsApi;
    private static QuakesClient Instance;

    public QuakesClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        usgsApi = retrofit.create(UsgsApi.class);
    }

    public static QuakesClient getInstance() {
        if(null == Instance) {
            Instance = new QuakesClient();
        }
        return Instance;
    }

    public Call<JsonObject> getQuakes (String format, int minMag, int limit, String orderBy) {
        return usgsApi.getQuakes(format, minMag, limit, orderBy);
    }
}
