package com.example.watchcontoller;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // MediaPlayer 객체생성
    MediaPlayer mediaPlayer;

    // 시작버튼
    Button close_start_btn;
    //종료버튼
    Button stopButton;
    Button far_start_btn;
    Button rotate_start_btn;
    Button flip_start_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        close_start_btn = findViewById(R.id.close_start);
        stopButton = findViewById(R.id.stop);
        far_start_btn =findViewById(R.id.far_start);
        rotate_start_btn = findViewById(R.id.rotate_start);
        flip_start_btn = findViewById(R.id.flip_start);

        close_start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // MediaPlayer 객체 할당
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.close);
                mediaPlayer.start();
            }
        });

        far_start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // MediaPlayer 객체 할당
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.far);
                mediaPlayer.start();
            }
        });

        flip_start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // MediaPlayer 객체 할당
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.flip);
                mediaPlayer.start();
            }
        });

        rotate_start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // MediaPlayer 객체 할당
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rotate);
                mediaPlayer.start();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 정지버튼
                mediaPlayer.stop();
                // 초기화
                mediaPlayer.reset();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // MediaPlayer 해지
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
