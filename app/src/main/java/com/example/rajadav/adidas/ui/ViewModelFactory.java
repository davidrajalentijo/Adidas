package com.example.rajadav.adidas.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

//instantiate MainViewModel
public class ViewModelFactory implements ViewModelProvider.Factory {
    private Context context;

    public ViewModelFactory(Context context) {
        this.context = context;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new MainViewModel(context);
    }

}
