package com.example.rajadav.adidas.ui.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rajadav.adidas.R;
import com.example.rajadav.adidas.model.CompletedGoal;
import com.example.rajadav.adidas.model.Goal;

import java.text.DateFormat;
import java.util.List;

//Adapter to ProfileFragment to show the list of goals achieved
public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.DataAdapterViewHolder>{
    private List<CompletedGoal> mData;
    private final ProfileAdapter.DataAdapterOnClickHandler mClickHandler;

    public interface DataAdapterOnClickHandler {
        void onClick(CompletedGoal goal);
    }

    public ProfileAdapter(ProfileAdapter.DataAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public void onBindViewHolder(ProfileAdapter.DataAdapterViewHolder dataAdapterViewHolder, int position) {
        String dataToShow = mData.get(position).getTitle();
        dataAdapterViewHolder.mPoints.setText(mData.get(position).getPoints().toString());
        String value = mData.get(position).getDay() + "/" + mData.get(position).getMonth() + "/" + mData.get(position).getYear();
        dataAdapterViewHolder.mDate.setText(value);
        dataAdapterViewHolder.mDataTextView.setText(dataToShow);

        switch (mData.get(position).getTrophy()) {
            case Goal.BRONZE_REWARD:
                dataAdapterViewHolder.mImage.setImageResource(R.drawable.bronzemedal);
                break;
            case Goal.SILVER_REWARD:
                dataAdapterViewHolder.mImage.setImageResource(R.drawable.silvermedal);
                break;
            case Goal.GOLD_REWARD:
                dataAdapterViewHolder.mImage.setImageResource(R.drawable.goldmedal);
                break;
            case Goal.ZOMBIE_REWARD:
                dataAdapterViewHolder.mImage.setImageResource(R.drawable.if__zombie_rising_1573300);
                break;
        }
    }

    @Override
    public ProfileAdapter.DataAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_profile_goal, viewGroup, false);
        return new ProfileAdapter.DataAdapterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setGoalData(List<CompletedGoal> activityData) {
        mData = activityData;
        notifyDataSetChanged();
    }

    public class DataAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mDataTextView;
        public final TextView mDate;
        public final TextView mPoints;
        public final ImageView mImage;

        public DataAdapterViewHolder(View view) {
            super(view);
            mDataTextView = view.findViewById(R.id.tv_title_data);
            mDate = view.findViewById(R.id.tv_date);
            mPoints = view.findViewById(R.id.tv_points);
            mImage = view.findViewById(R.id.imageReward);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(mData.get(adapterPosition));
        }
    }
}
