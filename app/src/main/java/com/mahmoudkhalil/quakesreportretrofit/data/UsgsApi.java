package com.mahmoudkhalil.quakesreportretrofit.data;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsgsApi {

    @GET("query")
    Call<JsonObject> getQuakes(@Query("format") String format, @Query("minmagnitude") int minMag, @Query("limit") int limit, @Query("orderby") String orderBy);
}
