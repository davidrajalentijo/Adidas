package com.example.rajadav.adidas;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import com.example.rajadav.adidas.database.Goal;


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

        ViewModelFactory factory = new ViewModelFactory(this);
        MainViewModel model = ViewModelProviders.of(this, factory).get(MainViewModel.class);
        model.getGoals().observe(this, data ->{
            mDataAdapter.setGoalData(data);
        });
    }

    @Override
    public void onClick(Goal goal) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("USER_ID", goal.getId());
        startActivity(intent);
    }
}
