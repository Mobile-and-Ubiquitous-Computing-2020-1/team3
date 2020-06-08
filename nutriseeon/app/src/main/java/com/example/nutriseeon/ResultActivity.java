package com.example.nutriseeon;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class ResultActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private boolean[] nutriSet;
    private String[] nutriVal;
    private TextView tv_nutriName;
    private TextView tv_nutriVal;
    private TextView tv_otherName;
    private TextView tv_otherVal;
    private int numSummary = 0;
    MainActivity.Nutritions[] nutritions = MainActivity.Nutritions.values();

    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Log.e("LOG", "Result activity onCreate");

        initialize();
        nutriText();

        Button ttsButton = (Button) findViewById(R.id.ttsButton);
        ttsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nutriTTS();
            }
        });

    }
    // setup TTS
    public void onInit(int initStatus) {

        // check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if (tts.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE)
                tts.setLanguage(Locale.US);
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...",
                    Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(tts != null){
            tts.stop();
            tts.shutdown();
        }
    }
    protected void initialize() {
        tv_nutriName = (TextView) findViewById(R.id.tv_nutriName);
        tv_nutriVal = (TextView) findViewById(R.id.tv_nutriVal);
        tv_otherName = (TextView) findViewById(R.id.tv_otherName);
        tv_otherVal = (TextView) findViewById(R.id.tv_otherVal);
        Intent intent = getIntent();
        nutriSet = SettingActivity.nutriSet;
        nutriVal = intent.getStringArrayExtra("nutriVal");
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }
    protected void nutriText() {
        String nutriNameStr = "";
        String nutriValStr = "";
        String otherNameStr = "";
        String otherValStr = "";
        String spaces = "      ";
        int nutriNum = nutriSet.length;

        for (int i=0; i<nutriNum; i++) {
            if (!nutriVal[i].contentEquals("")) {
                if (nutriSet[i]) {
                    nutriNameStr += nutritions[i].getName();
                    nutriNameStr += "\n";
                    nutriValStr += nutriVal[i];
                    nutriValStr += "\n";
                    numSummary += 1;
                } else {
                    otherNameStr += nutritions[i].getName();
                    otherNameStr += "\n";
                    otherValStr += nutriVal[i];
                    otherValStr += "\n";
                }
            }
        }


        tv_nutriName.setText(nutriNameStr);
        tv_nutriVal.setText(nutriValStr);
        tv_otherName.setText(otherNameStr);
        tv_otherVal.setText(otherValStr);
    }

    protected void nutriTTS() {
        Log.e("LOG", "nutriTTS");

        TextView tv_important = (TextView) findViewById(R.id.tv_important);
        TextView tv_other = (TextView) findViewById(R.id.tv_other);

        tts.setPitch(1.5f);
        tts.setSpeechRate(1.0f);

        int numNutri = nutriSet.length;
        String utteranceId=this.hashCode() + "";
        tts.speak(tv_important.getText().toString(), TextToSpeech.QUEUE_ADD, null, utteranceId);
        for (int i=0; i<numNutri; i++) {
            if (nutriSet[i] && !nutriVal[i].contentEquals("")) {
                tts.speak(nutritions[i].getName(), TextToSpeech.QUEUE_ADD, null, utteranceId);
                tts.speak(nutriVal[i], TextToSpeech.QUEUE_ADD, null, utteranceId);
            }
        }
        tts.speak(tv_other.getText().toString(), TextToSpeech.QUEUE_ADD, null, utteranceId);
        for (int i=0; i<numNutri; i++) {
            if (!nutriSet[i] && !nutriVal[i].contentEquals("")) {
                tts.speak(nutritions[i].getName(), TextToSpeech.QUEUE_ADD, null, utteranceId);
                tts.speak(nutriVal[i], TextToSpeech.QUEUE_ADD, null, utteranceId);
            }
        }

    }

}