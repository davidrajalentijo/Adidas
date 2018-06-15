package com.example.rajadav.adidas;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainViewModel extends ViewModel{

    //private MutableLiveData<String[]> mCurrentName;
    private MutableLiveData<List<String>> titleList;
    private String[] dummyDataTitle = {
            "Easy walk steps",
            "Medium walk steps",
            "Hard walk steps",
            "Walk some distance",
            "Quick Run",
            "Medium Run",
    };

    LiveData<List<String>> getTitleList() {

        if (titleList == null) {
            titleList = new MutableLiveData<>();
            loadtitleList();
        }
        Log.d("getTitleList", titleList.getValue().toString());
        return titleList;
    }

    private void loadtitleList() {
        List<String> titleStringList = Arrays.asList(dummyDataTitle);
        Log.d("loadtitleList", titleStringList.toString());
        titleList.setValue(titleStringList);


    }



}
