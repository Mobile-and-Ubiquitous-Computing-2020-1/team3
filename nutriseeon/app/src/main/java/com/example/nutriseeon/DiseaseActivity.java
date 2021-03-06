package com.example.nutriseeon;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.TextView;



public class DiseaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        final CheckBox cb1 = (CheckBox)findViewById(R.id.checkBox1);
        final CheckBox cb2 = (CheckBox)findViewById(R.id.checkBox2);
        final CheckBox cb3 = (CheckBox)findViewById(R.id.checkBox3);

        cb1.setChecked(pref.getBoolean("check1", false));
        cb2.setChecked(pref.getBoolean("check2", false));
        cb3.setChecked(pref.getBoolean("check3", false));

        Button b = (Button)findViewById(R.id.buttonSave);
        Log.e("LOG", "setDisease created");

        b.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("LOG", "setDisease click");

                Intent intent = new Intent();
                if (cb1.isChecked()) {
                    intent.putExtra(String.valueOf(MainActivity.Nutritions.CARBOHYDRATES), true);
                    intent.putExtra(String.valueOf(MainActivity.Nutritions.SUGARS), true);
                    intent.putExtra(String.valueOf(MainActivity.Nutritions.FAT), true);
                }
                if (cb2.isChecked()) {
                    intent.putExtra(String.valueOf(MainActivity.Nutritions.CARBOHYDRATES), true);
                    intent.putExtra(String.valueOf(MainActivity.Nutritions.SUGARS), true);
                }
                if (cb3.isChecked()) {
                    intent.putExtra(String.valueOf(MainActivity.Nutritions.FAT), true);
                    intent.putExtra(String.valueOf(MainActivity.Nutritions.SODIUM), true);
                }
                setResult(RESULT_OK, intent);
                finish();
            } // end onClick
        }); // end setOnClickListener
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Log.e("LOG", "setDisease stop");

        // Diabetes - Carbohydrate, Sugar, Fat
        CheckBox cb1 = (CheckBox)findViewById(R.id.checkBox1);
        // Obesity - Carbohydrate, Sugar
        CheckBox cb2 = (CheckBox)findViewById(R.id.checkBox2);
        // High pressure -  Fat, Sodium
        CheckBox cb3 = (CheckBox)findViewById(R.id.checkBox3);

        editor.putBoolean("check1", cb1.isChecked());
        editor.putBoolean("check2", cb2.isChecked());
        editor.putBoolean("check3", cb3.isChecked());
        editor.apply();
    }
}