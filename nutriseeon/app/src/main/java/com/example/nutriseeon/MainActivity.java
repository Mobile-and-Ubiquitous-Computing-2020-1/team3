package com.example.nutriseeon;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private int REQUEST_CODE = 1;
    public int num = 5;
    public boolean[] nutriSet = new boolean[num];
    private boolean[] nutriSetFromD = new boolean[num];
    private boolean[] nutriSetFromN = new boolean[num];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button settingButton = (Button) findViewById(R.id.settingButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                intent.putExtra("num", num);
            }
        });

        Button nutriButton = (Button) findViewById(R.id.nutriButton);
        nutriButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Log.e("LOG", "main received");
                boolean[] resSet = data.getBooleanArrayExtra("result_setting");
                String result = "Target nutritions: \n";
                if(resSet[0]) result += "\nCarbohydrate";
                if(resSet[1]) result += "\nProtein";
                if(resSet[2]) result += "\nFat";
                if(resSet[3]) result += "\nSodium";
                if(resSet[4]) result += "\nSugar";
                Log.e("LOG", "main res: "+result);
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }
        }
    }
}