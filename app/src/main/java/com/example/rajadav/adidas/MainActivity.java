package com.example.rajadav.adidas;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements DataAdapter.DataAdapterOnClickHandler{

    private TextView mTitleTextView;
    private DataAdapter mDataAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_data);
        mTitleTextView = (TextView) findViewById(R.id.tv_title_data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mDataAdapter = new DataAdapter(this);
        mRecyclerView.setAdapter(mDataAdapter);

        loadFinalData();
    }

    private void loadFinalData() {

        String[] dummyDataTitle = {
                "Easy walk steps",
                "Medium walk steps",
                "Hard walk steps",
                "Walk some distance",
                "Quick Run",
                "Medium Run",
        };

        mDataAdapter.setWeatherData(dummyDataTitle);

    }

    @Override
    public void onClick(String activitydone) {

        Context context = this;
        //Toast.makeText(context, activitydone, Toast.LENGTH_SHORT).show();
        Class destinationActivity = DetailActivity.class;
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, activitydone);
        startActivity(intent);

    }

}
