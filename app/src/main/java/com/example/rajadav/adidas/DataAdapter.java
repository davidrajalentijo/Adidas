package com.example.rajadav.adidas;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rajadav.adidas.database.Goal;

import java.util.List;



public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataAdapterViewHolder> {

    private List<Goal> mData;
    private final DataAdapterOnClickHandler mClickHandler;

    public interface DataAdapterOnClickHandler {
        void onClick(Goal goal);
    }

    public DataAdapter(DataAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class DataAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView mDataTextView;

        public DataAdapterViewHolder (View view) {
            super(view);
            mDataTextView = (TextView) view.findViewById(R.id.tv_title_data);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(mData.get(adapterPosition));
        }
    }


    @Override
    public void onBindViewHolder(DataAdapterViewHolder dataAdapterViewHolder, int position){
        String dataToShow = mData.get(position).getTitle();
        dataAdapterViewHolder.mDataTextView.setText(dataToShow);
    }

    @Override
    public DataAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item_goal;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new DataAdapterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setGoalData(List<Goal> activityData) {
        mData = activityData;
        notifyDataSetChanged();
    }


}
