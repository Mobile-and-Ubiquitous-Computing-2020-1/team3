package com.example.nutriseeon;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;

public class SettingActivity extends AppCompatActivity {
    MainActivity.Nutritions[] nutritions = MainActivity.Nutritions.values();
    public static boolean[] nutriSet = new boolean[MainActivity.Nutritions.values().length];
    private int REQUEST_DISEASE = 2;
    private int REQUEST_NUTRI = 3;

    private boolean[] nutriSetFromD = new boolean[nutriSet.length];
    private boolean[] nutriSetFromN = new boolean[nutriSet.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button button1 = (Button) findViewById(R.id.diseaseManageButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DiseaseActivity.class);
                startActivityForResult(intent, REQUEST_DISEASE);
            }
        });
        Button button2 = (Button) findViewById(R.id.nutriManageButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NutriActivity.class);
                startActivityForResult(intent, REQUEST_NUTRI);
            }
        });
        Button button3 = (Button)findViewById(R.id.buttonSave3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMain();
            }
        });

    }

    private void mergeResults() {
        for (int i=0; i<nutriSet.length; i++) {
            nutriSet[i] = nutriSetFromD[i] || nutriSetFromN[i];
        }
    }
    private void updateFromResult(boolean[] resArr, Intent data) {
        Arrays.fill(resArr, false);
        for (int i=0; i<nutriSet.length; i++) {
            if (data.getBooleanExtra(String.valueOf(nutritions[i]),
                    false))
                resArr[i] = true;
        }
        mergeResults();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_DISEASE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.e("LOG", "setDisease received");
                updateFromResult(nutriSetFromD, data);
                printNutri();
                //returnToMain();
            }
        } else if (requestCode == REQUEST_NUTRI) {
            if (resultCode == Activity.RESULT_OK) {
                Log.e("LOG", "setNutri received");
                updateFromResult(nutriSetFromN, data);
                printNutri();
                //returnToMain();
            }
        }
    }

    void printNutri() {
        String result = "Target nutritions: \n";
        for(int i=0; i<nutriSet.length; i++){
            if (nutriSet[i]) {
                result += "\n";
                result += nutritions[i].getName();
            }
        }
        Log.e("LOG", "main res: "+result);
        Toast.makeText(SettingActivity.this, result, Toast.LENGTH_LONG).show();
    }
    void returnToMain() {
        Log.e("LOG", "setting return");
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        for (int i=0; i<nutriSet.length; i++) {
            editor.putBoolean(String.valueOf(nutritions[i]), nutriSet[i]);
        }
        editor.apply();
        printNutri();
        Intent intentSetting = new Intent();
        setResult(RESULT_OK, intentSetting);
        finish();
    }


}