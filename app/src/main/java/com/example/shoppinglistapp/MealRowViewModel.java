package com.example.shoppinglistapp;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import java.util.List;

public class MealRowViewModel extends AndroidViewModel{

    private MealRepository repository;
    private LiveData<List<MealRow>> productsForMeal;
    public MutableLiveData<Integer> mealId = new MutableLiveData<Integer>();


    public MealRowViewModel(@NonNull Application application)
    {
        super(application);
        repository = new MealRepository(application);

    }

    public long insert(MealRow mealRow)
    {
        return repository.insertMealRow(mealRow);
    }

    public void update(MealRow mealRow)
    {
        repository.updateMealRow(mealRow);
    }

    public void delete(MealRow mealRow)
    {
        repository.deleteMealRow(mealRow);
    }

    public LiveData<String> GetProductName(int id){ return repository.GetProductName(id);}

    public LiveData<String> GetProductUnit(int id){return repository.GetProductUnit(id);}

    public LiveData<List<MealRow>> getAllMealRows(){return repository.getAllMealRows();}


    public LiveData<List<MealRow>> getProductsForMeal(int MealID)
    {
        return repository.getProductsForMeal(MealID);
    }

    public List<MealRow> getProductsForMealStatic(int MealID)
    {
        return repository.getProductsForMealStatic(MealID);
    }


    public LiveData<List<MealRow>> getFlaggedMeals(int MealID)
    {
        return repository.getFlaggedMeals(MealID);
    }



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
}
