package com.example.nutriseeon;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private int SETTING_REQUEST_CODE = 1;
    private int DEVICE_REQUEST_CODE = 10;
    private int PERMISSION_REQUEST_CAMERA = 1001;
    private int PERMISSION_REQUEST_BLUETOOTH = 1002;

    //public boolean[] nutriSet = new boolean[Nutritions.values().length];
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private String device_name = "";
    private String device_address = "";

    public enum Nutritions implements CharSequence {
        CARBOHYDRATES("Carbohydrate"),
        PROTEIN("Protein"),
        FAT("Fat"),
        SODIUM("Sodium"),
        SUGARS("Sugars"),
        SATFAT("SaturatedFat"),
        FIBER("DietaryFiber"),
        TRANSFAT("TransFat"),
        CHOLESTEROL("Cholesterol"),
        CALCIUM("Calcium"),
        CALORIES("Calories");
        private String name;

        Nutritions(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        // Unused functions
        @Override
        public int length() {
            return 0;
        }

        @Override
        public char charAt(int index) {
            return 0;
        }

        @NonNull
        @Override
        public CharSequence subSequence(int start, int end) {
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPermission();
        Button BleButton = (Button) findViewById(R.id.bleButton);
        BleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DeviceScanActivity.class);
                startActivityForResult(intent, DEVICE_REQUEST_CODE);
            }
        });
        Button settingButton = (Button) findViewById(R.id.settingButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivityForResult(intent, SETTING_REQUEST_CODE);
            }
        });

        Button nutriButton = (Button) findViewById(R.id.nutriButton);
        nutriButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                if (device_name == "" | device_address == "") {
                    Log.e("LOG", "device_address: null");
                    Toast.makeText(MainActivity.this, "No connected device!", Toast.LENGTH_LONG).show();
                } else {
                    intent.putExtra(EXTRAS_DEVICE_NAME, device_name);
                    intent.putExtra(EXTRAS_DEVICE_ADDRESS, device_address);
                }
                startActivity(intent);
            }
        });
        Button testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), testActivity.class);
                intent.putExtra("nutriSet", SettingActivity.nutriSet);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SETTING_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.e("LOG", "main received");
            } else {
                Toast.makeText(MainActivity.this, "Setting Failed", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == DEVICE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.e("LOG", "main: device connected");
                device_name = data.getStringExtra(this.EXTRAS_DEVICE_NAME);
                device_address = data.getStringExtra(this.EXTRAS_DEVICE_ADDRESS);
            }
        }
    }

    void setPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
            Toast.makeText(this,"Need permissions",Toast.LENGTH_LONG).show();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},
                    PERMISSION_REQUEST_BLUETOOTH);

            Toast.makeText(this,"Need permissions",Toast.LENGTH_LONG).show();
        }
    }
}