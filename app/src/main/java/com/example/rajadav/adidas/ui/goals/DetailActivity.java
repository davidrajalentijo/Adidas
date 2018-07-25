package com.example.rajadav.adidas.ui.goals;

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

import com.example.rajadav.adidas.model.CompletedGoal;
import com.example.rajadav.adidas.ui.MainViewModel;
import com.example.rajadav.adidas.R;
import com.example.rajadav.adidas.ui.ViewModelFactory;
import com.example.rajadav.adidas.model.Goal;
import com.example.rajadav.adidas.model.Reward;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
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
    private TextView mSteps;
    private ProgressBar mProgressBar;
    private ImageView mImageView;
    private OnDataPointListener mListener;

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
        mSteps = findViewById(R.id.tv_number_steps);

        ViewModelFactory factory = new ViewModelFactory(this);
        model = ViewModelProviders.of(this, factory).get(MainViewModel.class);

        Intent intentThatStartedThisActivity = getIntent();
        mId = intentThatStartedThisActivity.getIntExtra(INTENT_TAG, 0);

        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_LOCATION_SAMPLE, FitnessOptions.ACCESS_WRITE)
                .addDataType(DataType.TYPE_LOCATION_SAMPLE, FitnessOptions.ACCESS_READ)
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
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startTime = cal.getTimeInMillis();

        DataReadRequest.Builder builder = new DataReadRequest.Builder()
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(1, TimeUnit.DAYS);

        if (goal.getType().equals("step")) {
            builder.aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA);

        } else if (goal.getType().equals("walking_distance") || goal.getType().equals("running_distance")) {
            builder.aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA);
        }

        // Note: Fitness.SensorsApi.findDataSources() requires the ACCESS_FINE_LOCATION permission.
        Fitness.getSensorsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .findDataSources(
                        new DataSourcesRequest.Builder()
                                .setDataTypes(DataType.TYPE_STEP_COUNT_DELTA, DataType.TYPE_DISTANCE_DELTA)
                                .setDataSourceTypes(DataSource.TYPE_DERIVED)
                                .build())
                .addOnSuccessListener(
                        new OnSuccessListener<List<DataSource>>() {
                            @Override
                            public void onSuccess(List<DataSource> dataSources) {
                                for (DataSource dataSource : dataSources) {
                                    if (dataSource.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA)
                                         && mListener==null   && goal.getType().equals("step")) {
                                        Log.i(LOG_TAG, "Data source for STEPS found!  Registering.    "+ dataSource);
                                        registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_DELTA, goal);
                                    }
                                    else if (dataSource.getDataType().equals(DataType.TYPE_DISTANCE_DELTA) && mListener==null && (goal.getType().equals("walking_distance") || goal.getType().equals("running_distance"))){
                                        Log.i(LOG_TAG, "Data source for DISTANCE found!  Registering.    "+ dataSource);
                                        registerFitnessDataListener(dataSource, DataType.TYPE_DISTANCE_DELTA, goal);
                                    }
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(LOG_TAG, "failed", e);
                            }
                        });

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
                                        showStepsDataSet(goal, goal.getReward(), dataSet);
                                    }
                                    else if(dataSet.getDataType().getName().equals("com.google.distance.delta")) {
                                        showDistanceDataSet(goal, goal.getReward(), dataSet);
                                    }
                                }
                            }
                        } else if (dataReadResponse.getDataSets().size() > 0) {
                            for (DataSet dataSet : dataReadResponse.getDataSets()) {
                                if (dataSet.getDataType().getName().equals("com.google.step_count.delta")) {
                                    showStepsDataSet(goal, goal.getReward(), dataSet);
                                }
                                else if(dataSet.getDataType().getName().equals("com.google.distance.delta")) {
                                    showDistanceDataSet(goal,goal.getReward(), dataSet);
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

    /**
     * Registers a listener with the Sensors API for the provided {@link DataSource} and {@link
     * DataType} combo.
     */
    private void registerFitnessDataListener(DataSource dataSource, DataType dataType, Goal goal) {

        // [START register_data_listener]
        mListener =
                new OnDataPointListener() {
                    @Override
                    public void onDataPoint(DataPoint dataPoint) {
                        for (Field field : dataPoint.getDataType().getFields()) {
                            Value val = dataPoint.getValue(field);
                            if (dataType.getName().equals("com.google.step_count.delta")){
                                livesteps(val.asInt(), goal);
                            }
                            else if (dataType.getName().equals("com.google.distance.delta")){
                                float a = val.asFloat();
                                int b = Math.round(a);
                                livedistance(b, goal);
                            }
                        }
                    }
                };

        Fitness.getSensorsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .add(
                        new SensorRequest.Builder()
                                .setDataSource(dataSource)
                                .setDataType(dataType) // Can't be omitted.
                                .setSamplingRate(10, TimeUnit.SECONDS)
                                .build(),
                        mListener)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i(LOG_TAG, "Listener registered!");
                                } else {
                                    Log.e(LOG_TAG, "Listener not registered.", task.getException());
                                }
                            }
                        });
        // [END register_data_listener]
    }

    /** Unregisters the listener with the Sensors API. */
    private void unregisterFitnessDataListener() {
        if (mListener == null) {
            // This code only activates one listener at a time.  If there's no listener, there's
            // nothing to unregister.
            return;
        }

        // [START unregister_data_listener]
        // Waiting isn't actually necessary as the unregister call will complete regardless,
        // even if called from within onStop, but a callback can still be added in order to
        // inspect the results.
        Fitness.getSensorsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .remove(mListener)
                .addOnCompleteListener(
                        new OnCompleteListener<Boolean>() {
                            @Override
                            public void onComplete(@NonNull Task<Boolean> task) {
                                if (task.isSuccessful() && task.getResult()) {
                                    Log.i(LOG_TAG, "Listener was removed!");
                                } else {
                                    Log.i(LOG_TAG, "Listener was not removed.");
                                }
                            }
                        });
        // [END unregister_data_listener]
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterFitnessDataListener();
    }

    /*Method to show the steps done*/
    private void showStepsDataSet(Goal goal,Reward reward, DataSet dataSet) {

        for (DataPoint dp : dataSet.getDataPoints()) {

            if (goal.getSteps() ==dp.getValue(Field.FIELD_STEPS).asInt()){
                mProgressBar.setProgress(dp.getValue(Field.FIELD_STEPS).asInt());
            }
            else{
                goal.setSteps(dp.getValue(Field.FIELD_STEPS).asInt());
                model.updateGoal(goal);
                mProgressBar.setProgress(dp.getValue(Field.FIELD_STEPS).asInt());
            }

            mSteps.setText(getResources().getString(R.string.detail_steps_done, dp.getValue(Field.FIELD_STEPS).asInt()));
            chekCompleteGoal(goal, reward, dp.getValue(Field.FIELD_STEPS).asInt());
        }
    }

    /*Method to show the distance done*/
    private void showDistanceDataSet(Goal goal,Reward reward, DataSet dataSet) {
        for (DataPoint dp : dataSet.getDataPoints()) {
            float a = dp.getValue(Field.FIELD_DISTANCE).asFloat();
            int b = Math.round(a);

            if (goal.getDistance() == b){
                mSteps.setText(getResources().getString(R.string.detail_distance_done, b));
                mProgressBar.setProgress(b);
            }
            else{
                goal.setDistance(b);
                model.updateGoal(goal);
                mProgressBar.setProgress(b);
            }
            mSteps.setText(getResources().getString(R.string.detail_distance_done, b));
            chekCompleteGoal(goal, reward, b);
        }
    }

    /*Method to check if the goal is completed and show rewards*/
    private void chekCompleteGoal(Goal goal,Reward reward, int datadone) {
        if (datadone >= goal.getGoal()) {
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

            Calendar cal = Calendar.getInstance();
            Date date = new Date();
            CompletedGoal newCompleted = new CompletedGoal();
            newCompleted.setGoalid(goal.getId());
            newCompleted.setDay(cal.get(Calendar.DAY_OF_MONTH));
            newCompleted.setMonth((cal.get(Calendar.MONTH ) + 1));
            newCompleted.setYear(cal.get(Calendar.YEAR));
            newCompleted.setTitle(goal.getTitle());
            newCompleted.setPoints(reward.getPoints());
            newCompleted.setTrophy(reward.getTrophy());
            newCompleted.setHour(date.getHours());
            newCompleted.setMinutes(date.getMinutes());
            newCompleted.setSeconds(date.getSeconds());
            model.insertGoalCompletedIfNotExist(newCompleted);

            mPoints.setText(getResources().getString(R.string.detail_points_earned, reward.getPoints()));
            mPoints.setVisibility(View.VISIBLE);
        }
        else {
            mStatus.setVisibility(View.INVISIBLE);
        }
    }

    /*Method to update the steps done*/
    private void livesteps(int value, Goal goal){
        int finalsteps = goal.getSteps() + value;
            goal.setSteps(finalsteps);
            model.updateGoal(goal);
            mSteps.setText(getResources().getString(R.string.detail_steps_done, finalsteps));
            mProgressBar.setProgress(finalsteps);
    }

    /*Method to update the distance done*/
    private void livedistance(int value, Goal goal){
        int finaldistance = goal.getDistance() + value;
        goal.setDistance(finaldistance);
        model.updateGoal(goal);
        mSteps.setText(getResources().getString(R.string.detail_distance_done, finaldistance));
        mProgressBar.setProgress(finaldistance);
    }
}
