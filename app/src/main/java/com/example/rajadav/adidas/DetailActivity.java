package com.example.rajadav.adidas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView mDisplayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDisplayText = (TextView) findViewById(R.id.tv_passing_data);

        Intent intentThatStartedThisActivity = getIntent();


        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {

            String textEntered = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);

            mDisplayText.setText(textEntered);
        }
    }
}
