package com.example.nutriseeon;

import android.Manifest;
import android.app.Activity;
<<<<<<< HEAD
import android.content.Intent;
=======
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
>>>>>>> c2a1c2c148c7cbe95a924fe45da8b6cf0c6326dd
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private int REQUEST_CODE = 1;
    private int PERMISSION_REQUEST_CAMERA = 1001;
    public int num = 5;
    public boolean[] nutriSet = new boolean[num];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPermission();
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
                intent.putExtra("nutriSet", nutriSet);
                startActivity(intent);
            }
        });
        Button testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), testActivity.class);
                intent.putExtra("nutriSet", nutriSet);
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
                nutriSet = data.getBooleanArrayExtra("result_setting");
                String result = "Target nutritions: \n";
                if(nutriSet[0]) result += "\nCarbohydrate";
                if(nutriSet[1]) result += "\nProtein";
                if(nutriSet[2]) result += "\nFat";
                if(nutriSet[3]) result += "\nSodium";
                if(nutriSet[4]) result += "\nSugar";
                Log.e("LOG", "main res: "+result);
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }
        }
    }

    void setPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN},
                    PERMISSION_REQUEST_CAMERA);
            Toast.makeText(this,"Need permissions",Toast.LENGTH_LONG).show();
        }
    }
}