package com.example.shoppinglistapp;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface MealRowDao {

    @Insert
    long insert(MealRow mealRow);

    @Update
    void update(MealRow mealRow);

    @Delete
    void delete(MealRow mealRow);

    @Query("SELECT * FROM tbl_Meals_Row  ORDER BY id")
     LiveData<List<MealRow>> getAllMeals();

    @Query("SELECT MR.id, MR.MealId, MR.ProductId, MR.Quantity, P.name 'ProductName', P.unit 'ProductUnit', MR.insertFlag, MR.deleteFlag FROM " +
            " tbl_Meals_Row MR " +
            "INNER JOIN tbl_Products P ON MR.ProductId = P.id WHERE MR.MealId = :mealID AND MR.deleteFlag = 0" +
            " ORDER BY 1 ASC" )
     LiveData<List<MealRow>> getProductsForMeal(int mealID);

    @Query("SELECT MR.id, MR.MealId, MR.ProductId, MR.Quantity, P.name 'ProductName', P.unit 'ProductUnit', MR.insertFlag, MR.deleteFlag FROM " +
            " tbl_Meals_Row MR " +
            "INNER JOIN tbl_Products P ON MR.ProductId = P.id WHERE MR.MealId = :mealID AND MR.deleteFlag = 0" +
            " ORDER BY 1 ASC" )
    List<MealRow> getProductsForMealStatic(int mealID);

    @Query("SELECT name FROM tbl_Products WHERE ID = :productID")
    LiveData<String> getProductName(int productID);

    @Query("SELECT unit FROM tbl_Products WHERE ID = :productID")
    LiveData<String> getProductUnit(int productID);




    @Query("DELETE FROM tbl_Meals_Row WHERE MealId = :mealID AND deleteFlag = 1")
    void deleteDeleted(int mealID);

    @Query("DELETE FROM tbl_Meals_Row WHERE MealId = :mealID AND insertFlag = 1")
    void deleteInserted(int mealID);


    @Query("UPDATE tbl_Meals_Row SET deleteFlag = :flag WHERE MealId = :mealID AND id = :id ")
    void setDeletedFlagProd(int flag, int mealID, int id);

    @Query("UPDATE tbl_Meals_Row SET insertFlag = :flag WHERE MealId = :mealID AND ProductId = :productID ")
    void setInsertedFlagProd(int flag, int mealID, int productID);

    @Query("UPDATE tbl_Meals_Row SET deleteFlag = :flag ")
    void setDeletedFlagAll(int flag);

    @Query("UPDATE tbl_Meals_Row SET insertFlag = :flag")
    void setInsertedFlagAll(int flag);


    @Query("SELECT * FROM tbl_Meals_Row where insertFlag = 1 or deleteFlag = 1 and MealId = :mealID")
    LiveData<List<MealRow>> getFlaggedMeals(int mealID);











}



