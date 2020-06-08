package com.example.nutriseeon;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class NutriActivity extends AppCompatActivity {
    MainActivity.Nutritions[] nutritions = MainActivity.Nutritions.values();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutri);

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        final CheckBox cb1 = (CheckBox)findViewById(R.id.checkBox1);
        final CheckBox cb2 = (CheckBox)findViewById(R.id.checkBox2);
        final CheckBox cb3 = (CheckBox)findViewById(R.id.checkBox3);
        final CheckBox cb4 = (CheckBox)findViewById(R.id.checkBox4);
        final CheckBox cb5 = (CheckBox)findViewById(R.id.checkBox5);

        cb1.setChecked(pref.getBoolean("check1", false));
        cb2.setChecked(pref.getBoolean("check2", false));
        cb3.setChecked(pref.getBoolean("check3", false));
        cb4.setChecked(pref.getBoolean("check4", false));
        cb5.setChecked(pref.getBoolean("check5", false));

        Button b = (Button)findViewById(R.id.buttonSave2);
        Log.e("LOG", "setNutri created");

        b.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("LOG", "setNutri click");

                Intent intent = new Intent();
                if (cb1.isChecked())
                    intent.putExtra(String.valueOf(MainActivity.Nutritions.CARBOHYDRATES), true);
                if (cb2.isChecked())
                    intent.putExtra(String.valueOf(MainActivity.Nutritions.PROTEIN), true);
                if (cb3.isChecked())
                    intent.putExtra(String.valueOf(MainActivity.Nutritions.FAT), true);
                if (cb4.isChecked())
                    intent.putExtra(String.valueOf(MainActivity.Nutritions.SODIUM), true);
                if (cb5.isChecked())
                    intent.putExtra(String.valueOf(MainActivity.Nutritions.SUGARS), true);
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
        Log.e("LOG", "setNutri stop");

        CheckBox cb1 = (CheckBox)findViewById(R.id.checkBox1);
        CheckBox cb2 = (CheckBox)findViewById(R.id.checkBox2);
        CheckBox cb3 = (CheckBox)findViewById(R.id.checkBox3);
        CheckBox cb4 = (CheckBox)findViewById(R.id.checkBox4);
        CheckBox cb5 = (CheckBox)findViewById(R.id.checkBox5);

        editor.putBoolean("check1", cb1.isChecked());
        editor.putBoolean("check2", cb2.isChecked());
        editor.putBoolean("check3", cb3.isChecked());
        editor.putBoolean("check4", cb4.isChecked());
        editor.putBoolean("check5", cb5.isChecked());
        editor.apply();
    }
}