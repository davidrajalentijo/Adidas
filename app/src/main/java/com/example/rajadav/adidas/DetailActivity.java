package com.example.rajadav.adidas;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DetailActivity extends AppCompatActivity{

    private TextView mDisplayText;
    private TextView mDisplayDescription;
    private ProgressBar mProgressBar;
    private String DataRequired;
    public static final String LOG_TAG = "BasicHistoryApi";
    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mDisplayText = (TextView) findViewById(R.id.tv_passing_data);
        mDisplayDescription = (TextView) findViewById(R.id.tv_passing_description);
        mProgressBar = (ProgressBar) findViewById(R.id.goal_progressbar);

        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    fitnessOptions);
        } else {
            accessGoogleFit();
        }

        ViewModelFactory factory = new ViewModelFactory(this);
        MainViewModel model = ViewModelProviders.of(this, factory).get(MainViewModel.class);

        Intent intentThatStartedThisActivity = getIntent();
        int id = intentThatStartedThisActivity.getIntExtra("USER_ID", 0);

        model.getGoalDetail(id).observe(this, data->{
            DataRequired = data.getType();
            mDisplayText.setText(data.getTitle());
            mProgressBar.setMax(data.getGoal());
            mDisplayDescription.setText(data.getDescription());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                accessGoogleFit();
            }
        }
    }

    private void accessGoogleFit() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_WEEK, -1);
        long startTime = cal.getTimeInMillis();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(1, TimeUnit.DAYS)
                .build();

        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .readData(readRequest)
                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        Log.d(LOG_TAG, "onSuccess()");
                        if (dataReadResponse.getBuckets().size() > 0){
                            for (Bucket bucket : dataReadResponse.getBuckets()){
                                List<DataSet> dataSets = bucket.getDataSets();
                                for (DataSet dataSet: dataSets){
                                    switch (DataRequired){
                                        case "step": if (dataSet.getDataType().getName().equals("com.google.step_count.delta")){ showStepsDataSet(dataSet);}break;
                                        case "walking_distance":  if (dataSet.getDataType().getName().equals("com.google.distance.delta")){ showDistanceDataSet(dataSet);}; break ;
                                        case "running_distance":  if (dataSet.getDataType().getName().equals("com.google.distance.delta")){ showDistanceDataSet(dataSet);}; break ;
                                    }
                                }
                            }
                        }
                        else if (dataReadResponse.getDataSets().size() > 0){
                            for (DataSet dataSet: dataReadResponse.getDataSets()){
                                switch (DataRequired){
                                    case "step": if (dataSet.getDataType().getName().equals("com.google.step_count.delta")){ showStepsDataSet(dataSet);}break;
                                    case "walking_distance":  if (dataSet.getDataType().getName().equals("com.google.distance.delta")){ showDistanceDataSet(dataSet);}; break ;
                                    case "running_distance":  if (dataSet.getDataType().getName().equals("com.google.distance.delta")){ showDistanceDataSet(dataSet);}; break ;
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(LOG_TAG, "onFailure()", e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Log.d(LOG_TAG, "onComplete()");
                    }
                });
    }

    private void showStepsDataSet(DataSet dataSet) {
        for (DataPoint dp : dataSet.getDataPoints()) {
            for (Field field : dp.getDataType().getFields()) {
                mProgressBar.setProgress(dp.getValue(field).asInt());
            }
        }
    }

    private void showDistanceDataSet(DataSet dataSet) {
        for (DataPoint dp : dataSet.getDataPoints()) {
            for (Field field : dp.getDataType().getFields()) {
                float a = dp.getValue(Field.FIELD_DISTANCE).asFloat();
                int b = (int)Math.round(a);
                mProgressBar.setProgress(b);
            }
        }
    }

}
