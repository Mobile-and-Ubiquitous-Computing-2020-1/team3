package com.example.nutriseeon;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AndroidResponse {
    private int REQUEST_RESULT = 1;
    Context ctx;
    MainActivity.Nutritions[] nutritions = MainActivity.Nutritions.values();

    public AndroidResponse(Context context) {
        this.ctx = context;
    }

    String[] Done(boolean[] nutriSet, JSONObject retVal) throws JSONException {
        Log.e("LOG", "Response Done");

        String[] nutriVal = new String[MainActivity.Nutritions.values().length];

        for (int i=0; i<nutritions.length; i++){
            try {
                nutriVal[i] = retVal.getString(nutritions[i].getName());
            } catch (JSONException e) {
                nutriVal[i] = "";
            }
        }
        return nutriVal;

    }
}