package com.example.rajadav.adidas;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity implements DataAdapter.DataAdapterOnClickHandler{

    private TextView mTitleTextView;
    private DataAdapter mDataAdapter;
    private RecyclerView mRecyclerView;
    private MainViewModel mModel;

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
/*
        String[] dummyDataTitle = {
                "Easy walk steps",
                "Medium walk steps",
                "Hard walk steps",
                "Walk some distance",
                "Quick Run",
                "Medium Run",
        };
*/

        MainViewModel model = ViewModelProviders.of(this).get(MainViewModel.class);


        model.getTitleList().observe(this, titleList-> {

            Log.d("Checking All correct", "Receiving database update from LiveData");
                    mDataAdapter.setGoalData(titleList);
                });

            /*
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, fruitlist);
            // Assign adapter to ListView
            listView.setAdapter(adapter);
             });
*/


/*
        final Observer<String> goalObserver = new Observer <String>() {
            @Override
            public void onChanged(@Nullable final String Goal) {
                // Update the UI, in this case, a TextView.
                Log.d("onChanged", Goal);
                mDataAdapter.setGoalData(Goal);
            }
        };*/
        //model.getTitleList().observe(this,goalObserver);;
        //mModel.getTitleList().observe(this,goalObserver);
        //mModel.getTitleList().observe(this, goalObserver);
        //mModel.getCurrentName().observe(this, goalObserver);



    }

    @Override
    public void onClick(String activitydone) {

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, activitydone);
        startActivity(intent);

    }

}
