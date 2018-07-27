package com.example.rajadav.adidas.ui.goals;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rajadav.adidas.ui.MainViewModel;
import com.example.rajadav.adidas.R;
import com.example.rajadav.adidas.ui.ViewModelFactory;
import com.example.rajadav.adidas.model.Goal;

//Fragmente for the goals view where show all the goals
public class GoalFragment extends Fragment implements GoalAdapter.DataAdapterOnClickHandler {

    private GoalAdapter mGoalAdapter;
    private RecyclerView mRecyclerView;

    public GoalFragment() { }

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static GoalFragment newInstance(int sectionNumber) {
        GoalFragment fragment = new GoalFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_goal, container, false);

        mRecyclerView = rootView.findViewById(R.id.recyclerview_main_data);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mGoalAdapter = new GoalAdapter(this);
        mRecyclerView.setAdapter(mGoalAdapter);

        ViewModelFactory factory = new ViewModelFactory(getActivity().getApplicationContext());
        MainViewModel model = ViewModelProviders.of(this, factory).get(MainViewModel.class);
        model.getGoals().observe(this, data ->{
            mGoalAdapter.setGoalData(data);
        });

        return rootView;
    }

    @Override
    public void onClick(Goal goal) {
        startActivity(DetailActivity.newIntent(getActivity().getApplicationContext(), goal.getId()));
    }

}
