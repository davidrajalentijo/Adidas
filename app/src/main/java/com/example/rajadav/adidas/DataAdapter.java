package com.example.rajadav.adidas;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataAdapterViewHolder> {

    private String[] mActivityData;

    private final DataAdapterOnClickHandler mClickHandler;


    public interface DataAdapterOnClickHandler {

        void onClick(String activitydone);

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
            String activitydone = mActivityData[adapterPosition];
            mClickHandler.onClick(activitydone);
        }
    }


    @Override
    public void onBindViewHolder(DataAdapterViewHolder dataAdapterViewHolder, int position){
        String dataToShow = mActivityData[position];
        dataAdapterViewHolder.mDataTextView.setText(dataToShow);


    }

    @Override
    public DataAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.activity_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new DataAdapterViewHolder(view);

    }

    @Override
    public int getItemCount() {
        if(mActivityData == null){
            return 0;
        }
        return mActivityData.length;
    }

    public void setWeatherData(String[] activityData) {
        mActivityData = activityData;
        notifyDataSetChanged();
    }

}
