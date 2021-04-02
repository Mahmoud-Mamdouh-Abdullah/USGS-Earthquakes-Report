package com.mahmoudkhalil.quakesreportretrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import com.mahmoudkhalil.quakesreportretrofit.models.Earthquake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Logic {

    public static ArrayList<Earthquake> extractEarthQuakes(String jsonString) {

        ArrayList<Earthquake> EarthQuakes = new ArrayList<>();

        try {
            JSONObject rootObject = new JSONObject(jsonString);
            JSONArray featuresArray = rootObject.getJSONArray("features");
            for(int i = 0; i < featuresArray.length(); i ++) {
                JSONObject feature = featuresArray.getJSONObject(i);
                JSONObject prop = feature.getJSONObject("properties");
                long time = prop.getLong("time");
                double magnitude = prop.getDouble("mag");
                String url = prop.getString("url");
                Earthquake earthQuake = new Earthquake(magnitude, prop.getString("place"), time, url);
                EarthQuakes.add(earthQuake);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the EarthQuake JSON results", e);
        }
        return EarthQuakes;
    }

    /**
     * checking the internet connection
     * @return false if there is no internet
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
