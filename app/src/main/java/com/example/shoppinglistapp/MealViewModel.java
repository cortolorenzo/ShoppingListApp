package com.example.shoppinglistapp;


import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import java.util.List;

public class MealViewModel extends AndroidViewModel
{

    private MealRepository repository;
    private LiveData<List<Meal>> allMeals;



    public MealViewModel(@NonNull Application application)
    {
        super(application);
        repository = new MealRepository(application);
        allMeals = repository.getAllMeals();
    }

    public long insert(Meal meal)
    {
        return repository.insert(meal);
    }

    public void update(Meal meal)
    {
        repository.update(meal);
    }

    public void delete(Meal meal)
    {
        repository.delete(meal);
    }

    public LiveData<List<Meal>> getAllMeals()
    {
        return allMeals;
    }

    public LiveData<Meal> getLastMeal() {return repository.getLastMeal();}

    public void deleteMeal(int mealID) {repository.deleteMeal(mealID);}
    public void deleteMealRow(int mealID) {repository.deleteMealRow(mealID);}

    public void deleteDeleted(int mealID) {repository.deleteDeleted(mealID);}
    public void deleteInserted(int mealID) {repository.deleteInserted(mealID);}

    public void updateDeleted(MealRow mealRow)
    {
        repository.updateDeleted(mealRow);
    }
    public void updateInserted(MealRow mealRow)
    {
        repository.updateInserted(mealRow);
    }

    public void setDeleteFlagAll(int flag)
    {
        repository.setDeleteFlagAll(flag);
    }
    public void setInsertFlagAll(int flag)
    {
        repository.setInsertFlagAll(flag);
    }








}
