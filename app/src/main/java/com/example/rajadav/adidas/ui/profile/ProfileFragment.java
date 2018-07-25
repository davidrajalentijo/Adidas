package com.example.rajadav.adidas.ui.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rajadav.adidas.R;
import com.example.rajadav.adidas.model.CompletedGoal;
import com.example.rajadav.adidas.ui.MainViewModel;
import com.example.rajadav.adidas.ui.ViewModelFactory;

import java.util.Calendar;

public class ProfileFragment extends Fragment implements ProfileAdapter.DataAdapterOnClickHandler{

    private ProfileAdapter mProfileAdapter;
    private RecyclerView mRecyclerView;
    private TextView mTotalPoints;
    private TextView mTodayPoints;

    public ProfileFragment() {
        // Required empty public constructor
    }

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static ProfileFragment newInstance(int sectionNumber) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerview_main_data_completed);
        mTotalPoints = rootView.findViewById(R.id.tv_total_points);
        mTodayPoints = rootView.findViewById(R.id.tv_day_points);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mProfileAdapter = new ProfileAdapter(this);
        mRecyclerView.setAdapter(mProfileAdapter);

        ViewModelFactory factory = new ViewModelFactory(getActivity().getApplicationContext());
        MainViewModel model = ViewModelProviders.of(this, factory).get(MainViewModel.class);

        model.getGoalsAchieved().observe(this, data ->{
            mProfileAdapter.setGoalData(data);
        });

        Calendar cal = Calendar.getInstance();

        model.getPoints().observe(this, data ->{
            mTotalPoints.setText(getResources().getString(R.string.total_points, data.getTotalpoints()));
        });

        model.getDayPoints(cal.get(Calendar.DAY_OF_MONTH), (cal.get(Calendar.MONTH ) + 1),cal.get(Calendar.YEAR) ).observe(this, data ->{
            mTodayPoints.setText(getResources().getString(R.string.today_points, data.getTotalpoints()));
        });

        return rootView;
    }

    @Override
    public void onClick(CompletedGoal goal) {
        Log.d("Showing info", "date " + goal.getDay() + "/" + goal.getMonth() + "/" + goal.getYear() + " " + goal.getHour() + ":" + goal.getMinutes() + ":" + goal.getSeconds());
    }
}
