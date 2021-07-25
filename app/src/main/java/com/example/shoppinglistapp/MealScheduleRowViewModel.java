package com.example.shoppinglistapp;

import androidx.lifecycle.AndroidViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;


import java.sql.Date;
import java.util.List;

public class MealScheduleRowViewModel extends AndroidViewModel
{

    private MealScheduleRepository repository;

    public MealScheduleRowViewModel(@NonNull Application application)
    {
        super(application);
        repository = new MealScheduleRepository(application);
    }

    public LiveData<List<MealScheduleRow>> GetMealScheduleRows(int mealScheduleId){ return repository.getMealScheduleRows(mealScheduleId);}
    public LiveData<List<MealScheduleRow>> GetMealsForSchedule(){return repository.getMealsForSchedule();}


    public long insert(MealScheduleRow mealScheduleRow)
    {
        return repository.insert(mealScheduleRow);
    }

    public void deleteMealScheduleRow(MealScheduleRow mealScheduleRow)
    {
        repository.deleteMealScheduleRow(mealScheduleRow);
    }

    public void updateMealScheduleRow(MealScheduleRow mealScheduleRow)
    {
        repository.updateMealScheduleRow(mealScheduleRow);
    }

}
