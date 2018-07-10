package com.example.rajadav.adidas;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rajadav.adidas.database.Goal;
import com.example.rajadav.adidas.database.Reward;
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

//This Activity show one goal with detail and connect with Google Fit to receive the corresponding data
public class DetailActivity extends AppCompatActivity {
    private static final String INTENT_TAG = "GOAL_ID";
    public static final String LOG_TAG = "DetailActivity";
    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 100;

    private TextView mDisplayText;
    private TextView mDisplayDescription;
    private TextView mStatus;
    private TextView mPoints;
    private ProgressBar mProgressBar;
    private ImageView mImageView;

    private MainViewModel model;
    private int mId;

    public static Intent newIntent(Context context, int goalId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(INTENT_TAG, goalId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mDisplayText = findViewById(R.id.tv_passing_data);
        mDisplayDescription = findViewById(R.id.tv_passing_description);
        mProgressBar = findViewById(R.id.goal_progressbar);
        mImageView = findViewById(R.id.imageDisplay);
        mStatus = findViewById(R.id.tv_status);
        mPoints = findViewById(R.id.tv_points);

        ViewModelFactory factory = new ViewModelFactory(this);
        model = ViewModelProviders.of(this, factory).get(MainViewModel.class);

        Intent intentThatStartedThisActivity = getIntent();
        mId = intentThatStartedThisActivity.getIntExtra(INTENT_TAG, 0);

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
            getGoal(mId);
        }
    }

    private void getGoal(int id) { ;
        model.getGoalDetail(id).observe(this, data -> {
            mDisplayText.setText(data.getTitle());
            mProgressBar.setMax(data.getGoal());
            mDisplayDescription.setText(data.getDescription());
            accessGoogleFit(data);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                getGoal(mId);
            }
        }
    }

    private void accessGoogleFit(Goal goal) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_WEEK, -1);
        long startTime = cal.getTimeInMillis();

        DataReadRequest.Builder builder = new DataReadRequest.Builder()
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(1, TimeUnit.DAYS);

        if (goal.getType().equals("step")) {
            builder.aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA);
        } else if (goal.getType().equals("walking_distance") || goal.getType().equals("running_distance")) {
            builder.aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA);
        }

        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .readData(builder.build())
                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        Log.d(LOG_TAG, "onSuccess()");
                        if (dataReadResponse.getBuckets().size() > 0) {
                            for (Bucket bucket : dataReadResponse.getBuckets()) {
                                List<DataSet> dataSets = bucket.getDataSets();
                                for (DataSet dataSet : dataSets) {
                                    if (dataSet.getDataType().getName().equals("com.google.step_count.delta")) {
                                        showStepsDataSet(goal.getReward(), dataSet);
                                    }
                                    else if(dataSet.getDataType().getName().equals("com.google.distance.delta")) {
                                        showDistanceDataSet(goal.getReward(), dataSet);
                                    }
                                }
                            }
                        } else if (dataReadResponse.getDataSets().size() > 0) {
                            for (DataSet dataSet : dataReadResponse.getDataSets()) {
                                if (dataSet.getDataType().getName().equals("com.google.step_count.delta")) {
                                    showStepsDataSet(goal.getReward(), dataSet);
                                }
                                else if(dataSet.getDataType().getName().equals("com.google.distance.delta")) {
                                    showDistanceDataSet(goal.getReward(), dataSet);
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

    private void showStepsDataSet(Reward reward, DataSet dataSet) {
        for (DataPoint dp : dataSet.getDataPoints()) {
            mProgressBar.setProgress(dp.getValue(Field.FIELD_STEPS).asInt());
            chekCompleteGoal(reward);
        }
    }

    private void showDistanceDataSet(Reward reward, DataSet dataSet) {
        for (DataPoint dp : dataSet.getDataPoints()) {
            float a = dp.getValue(Field.FIELD_DISTANCE).asFloat();
            int b = Math.round(a);
            mProgressBar.setProgress(b);
            chekCompleteGoal(reward);
        }
    }

    private void chekCompleteGoal(Reward reward) {
        if (mProgressBar.getProgress() == mProgressBar.getMax()) {
            mStatus.setVisibility(View.VISIBLE);
            switch (reward.getTrophy()) {
                case Goal.BRONZE_REWARD:
                    mImageView.setImageResource(R.drawable.bronzemedal);
                    break;
                case Goal.SILVER_REWARD:
                    mImageView.setImageResource(R.drawable.silvermedal);
                    break;
                case Goal.GOLD_REWARD:
                    mImageView.setImageResource(R.drawable.goldmedal);
                    break;
                case Goal.ZOMBIE_REWARD:
                    mImageView.setImageResource(R.drawable.if__zombie_rising_1573300);
                    break;
            }
            mPoints.setText(getResources().getString(R.string.detail_points_earned, reward.getPoints()));
            mPoints.setVisibility(View.VISIBLE);
        } else {
            mStatus.setVisibility(View.INVISIBLE);
        }
    }

}
