package com.example.rajadav.adidas;


import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.rajadav.adidas.database.Goal;

//Recieve a list of Goals and show the title of the goal
public class MainActivity extends AppCompatActivity implements DataAdapter.DataAdapterOnClickHandler{
    private DataAdapter mDataAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerview_main_data);
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
        startActivity(DetailActivity.newIntent(this, goal.getId()));
    }
}
