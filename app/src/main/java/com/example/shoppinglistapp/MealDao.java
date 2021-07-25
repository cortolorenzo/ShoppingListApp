package com.example.shoppinglistapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MealDao
{

    @Insert
    long insert(Meal meal);

    @Update
    void update(Meal meal);

    @Delete
    void delete(Meal meal);

    @Query("SELECT * FROM tbl_Meals ORDER BY id")
    LiveData<List<Meal>> getAllMeals();

    @Query("SELECT * FROM tbl_Meals ORDER BY id DESC LIMIT 1")
    LiveData<Meal> getLastMeal();

    @Query("DELETE FROM tbl_Meals WHERE id = :mealID")
    void deleteNotSavedMeal(int mealID);

    @Query("DELETE FROM tbl_Meals_Row WHERE MealId = :mealID")
    void deleteNotSavedMealRows(int mealID);


}

