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
    private int REQUEST_DISEASE = 2;
    private int REQUEST_NUTRI = 3;

    private int num = 5;
    private boolean[] nutriSet = new boolean[num];
    private boolean[] nutriSetFromD = new boolean[num];
    private boolean[] nutriSetFromN = new boolean[num];

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
        for (int i=0; i<num; i++) {
            nutriSet[i] = nutriSetFromD[i] || nutriSetFromN[i];
        }
    }
    private void updateFromResult(boolean[] resArr, Intent data) {
        Arrays.fill(resArr, false);
        if (data.getBooleanExtra("Carbohydrate", false))
            resArr[0] = true;
        if (data.getBooleanExtra("Protein", false))
            resArr[1] = true;
        if (data.getBooleanExtra("Fat", false))
            resArr[2] = true;
        if (data.getBooleanExtra("Sodium", false))
            resArr[3] = true;
        if (data.getBooleanExtra("Sugar", false))
            resArr[4] = true;
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
        if(nutriSet[0]) result += "\nCarbohydrate";
        if(nutriSet[1]) result += "\nProtein";
        if(nutriSet[2]) result += "\nFat";
        if(nutriSet[3]) result += "\nSodium";
        if(nutriSet[4]) result += "\nSugar";
        Log.e("LOG", "main res: "+result);
        Toast.makeText(SettingActivity.this, result, Toast.LENGTH_LONG).show();
    }
    void returnToMain() {
        Log.e("LOG", "setting return");
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("nutri0", nutriSet[0]);
        editor.putBoolean("nutri1", nutriSet[1]);
        editor.putBoolean("nutri2", nutriSet[2]);
        editor.putBoolean("nutri3", nutriSet[3]);
        editor.putBoolean("nutri4", nutriSet[4]);
        editor.apply();
        Intent intentSetting = new Intent();

        String result = "";
        if(nutriSet[0]) result += "Carbohydrate\n";
        if(nutriSet[1]) result += "Protein\n";
        if(nutriSet[2]) result += "Fat\n";
        if(nutriSet[3]) result += "Sodium\n";
        if(nutriSet[4]) result += "Sugar\n";
        Log.e("LOG", "setting res: "+result);

        intentSetting.putExtra("result_msg", "here it goes");
        intentSetting.putExtra("result_setting", nutriSet);
        setResult(RESULT_OK, intentSetting);
        finish();
    }


}