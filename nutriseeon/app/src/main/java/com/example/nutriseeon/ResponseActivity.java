package com.example.nutriseeon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import org.json.JSONObject;

public class ResponseActivity extends AppCompatActivity {
    private int REQUEST_RESULT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);
        Intent intent = getIntent();
        String stage = intent.getStringExtra("stage");
        JSONObject retVal = intent.getSerializableExtra()("retVal");
        switch (stage) {
            case CameraActivity.ServiceState.DETECT_HAND:
                Log.e("STAGE?", "DETECTHAND");
                break;
            case CameraActivity.ServiceState.LOCATE_HAND:
                Log.e("STAGE?", "LOCATEHAND");
                break;
            case CameraActivity.ServiceState.ROTATE:
                Log.e("STAGE?", "ROTATE");
                break;
            case CameraActivity.ServiceState.FLIP:
                Log.e("STAGE?", "FLIP");
                break;
            case CameraActivity.ServiceState.DONE:
                Log.e("STAGE?", "DONE");
                response_Done(retVal)
                break;
            // result
        }
    }
    void response_Done(JSONObject retVal) {
        boolean[] nutriSet;
        String[] nutriName;
        String[] nutriVal;
        String[] otherName;
        String[] otherVal;

        Intent intent = new Intent(getApplicationContext(), ResponseActivity.class);
        intent.putExtra("stage", "DONE")
        intent.putExtra("nutriSet", nutriSet);
        startActivity(intent);



//        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
//        intent.putExtra("nutriSet", nutriSet);
//        intent.putExtra("nutriName", nutriName);
//        intent.putExtra("nutriVal", nutriVal);
//        intent.putExtra("otherName", otherName);
//        intent.putExtra("otherVal", otherVal);
//        startActivity(intent);
    }
}