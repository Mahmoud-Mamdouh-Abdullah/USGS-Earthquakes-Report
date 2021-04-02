package com.mahmoudkhalil.quakesreportretrofit.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.mahmoudkhalil.quakesreportretrofit.data.QuakesClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuakesViewModel extends ViewModel {
    private MutableLiveData<JsonObject> quakesMutableLiveData = new MutableLiveData<>();

    public void getQuakes(String format, int minMag, int limit, String orderBy) {
        QuakesClient.getInstance().getQuakes(format, minMag, limit, orderBy).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                quakesMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    public MutableLiveData<JsonObject> getQuakesMutableLiveData() {
        return quakesMutableLiveData;
    }
}
