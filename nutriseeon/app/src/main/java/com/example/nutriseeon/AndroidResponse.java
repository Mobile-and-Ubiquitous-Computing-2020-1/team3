package com.example.nutriseeon;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AndroidResponse {
    private int REQUEST_RESULT = 1;
    Context ctx;
    MainActivity.Nutritions[] nutritions = MainActivity.Nutritions.values();
    MediaPlayer mediaPlayer;

    public AndroidResponse(Context context) {
        this.ctx = context;
    }

    void Close() {
        mediaPlayer = MediaPlayer.create(ctx, R.raw.close);
        mediaPlayer.start();
    }
    void Far() {
        mediaPlayer = MediaPlayer.create(ctx, R.raw.far);
        mediaPlayer.start();
    }

    void Rotate() {
        mediaPlayer = MediaPlayer.create(ctx, R.raw.rotate);
        mediaPlayer.start();
    }

    void Flip() {
        mediaPlayer = MediaPlayer.create(ctx, R.raw.flip);
        mediaPlayer.start();
    }
    String[] Done(boolean[] nutriSet, JSONObject retVal) throws JSONException {
        Log.e("LOG", "Response Done");
        JSONArray jsonNames = retVal.names();
        for (int i=0; i<retVal.length(); i++) {
            Log.e("LOG", jsonNames.getString(i));
        }
        for(int i=0; i<nutritions.length; i++) {
            Log.e("LOG", nutritions[i].getName());
            try {
                Log.e("LOG", retVal.getString(nutritions[i].getName()));
            } catch (JSONException e) {
                Log.e("LOG", "");
            }
        }

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