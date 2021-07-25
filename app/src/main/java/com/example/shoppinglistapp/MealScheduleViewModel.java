package com.example.shoppinglistapp;

import androidx.lifecycle.AndroidViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;


import java.sql.Date;
import java.util.List;

public class MealScheduleViewModel extends AndroidViewModel
{


    private MealScheduleRepository repository;

    public MealScheduleViewModel(@NonNull Application application)
    {
        super(application);
        repository = new MealScheduleRepository(application);

    }


    public LiveData<List<MealSchedule>> GetAllMealSchedules(java.util.Date startDate, java.util.Date endDate){ return repository.getAllMealSchedules(startDate, endDate);}
    public LiveData<List<MealScheduleRow>> GetMealScheduleRows(int mealScheduleId){ return repository.getMealScheduleRows(mealScheduleId);}


    public long insert(MealSchedule mealSchedule)
    {
         return repository.insert(mealSchedule);
    }

    public void update(MealSchedule mealSchedule)
    {
         repository.updateMealSchedule(mealSchedule);
    }
}
