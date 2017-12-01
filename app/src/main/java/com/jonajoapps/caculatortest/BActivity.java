package com.jonajoapps.caculatortest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class BActivity extends AppCompatActivity {
    public static final String TAG = "BActivity";
    public static final String OBJECT = "Object";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        Intent wakeI = getIntent();
        TextView tv = (TextView) findViewById(R.id.titleFromA);

        SharedPreferences sp = getSharedPreferences("settings", MODE_PRIVATE);

        String str = sp.getString("shay", null);


        if (wakeI != null) {
            LineObject lo = wakeI.getParcelableExtra(OBJECT);
            tv.setText(lo.title);
        }


        findViewById(R.id.goBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(in);
            }
        });

        Log.w(TAG, "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy");
    }

}
