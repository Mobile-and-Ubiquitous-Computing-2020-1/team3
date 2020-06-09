package com.example.nutriseeon;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private int SETTING_REQUEST_CODE = 1;
    private int DEVICE_REQUEST_CODE = 10;


    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.CAMERA
    };


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
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
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


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}