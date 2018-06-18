package com.example.rajadav.adidas;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.example.rajadav.adidas.database.Goal;
import java.util.List;

public class MainViewModel extends ViewModel{

    private GoalsRepo goalRepo = new GoalsRepo();

    public LiveData<List<Goal>> getGoals() {
        return goalRepo.getGoals();
    }

}
