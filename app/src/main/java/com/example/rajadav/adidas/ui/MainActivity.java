package com.example.rajadav.adidas.ui;


import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.rajadav.adidas.R;
import com.example.rajadav.adidas.model.Goal;
import com.example.rajadav.adidas.ui.goals.DataAdapter;
import com.example.rajadav.adidas.ui.goals.DetailActivity;
import com.example.rajadav.adidas.ui.goals.GoalFragment;
import com.example.rajadav.adidas.ui.profile.ProfileFragment;

//Recieve a list of Goals and show the title of the goal
public class MainActivity extends AppCompatActivity implements DataAdapter.DataAdapterOnClickHandler{

    private ViewPager mViewPager;
    private MainActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*
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
        });*/

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new MainActivity.SectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public void onClick(Goal goal) {
        startActivity(DetailActivity.newIntent(this, goal.getId()));
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private Context mContext;

        public SectionsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("checking item fragment", "position " + position);
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0)
            {
            return GoalFragment.newInstance(position +1);}
            else if (position ==1){
                return ProfileFragment.newInstance(position +1);
            }
            else{
                return ProfileFragment.newInstance(position +1);
            }
            //return GoalFragment.newInstance(position +1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}
