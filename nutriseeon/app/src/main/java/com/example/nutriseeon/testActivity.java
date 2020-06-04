package com.example.nutriseeon;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class testActivity extends AppCompatActivity {
    private boolean[] nutriSet;
    private String[] nutriName;
    private String[] nutriVal;
    private String[] otherName;
    private String[] otherVal;
    private int REQUEST_RESULT = 1;

    private TextView tv_outPut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();
        nutriSet = intent.getExtras().getBooleanArray("nutriSet");
        tv_outPut = (TextView) findViewById(R.id.tv_output);

        nutriName = new String[]{"Carbohydrates", "Protein", "Fat", "Sodium", "Sugar"};
        nutriVal = new String[]{"10g", "20g", "30g", "40g", "50g"};
        otherName = new String[]{"Calories", "Saturated Fat", "Fiber"};
        otherVal = new String[]{"60kcal", "70mg", "80mg"};

        Intent resultIntent = new Intent(getApplicationContext(), ResultActivity.class);
        resultIntent.putExtra("nutriSet", nutriSet);
        resultIntent.putExtra("nutriName", nutriName);
        resultIntent.putExtra("nutriVal", nutriVal);
        resultIntent.putExtra("otherName", otherName);
        resultIntent.putExtra("otherVal", otherVal);

        startActivityForResult(resultIntent, REQUEST_RESULT);

        //String url = "http://";
        //NetworkTask networkTask = new NetworkTask(url, null);
        //networkTask.execute();

    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            tv_outPut.setText(s);
        }
    }
}