package com.example.shoppinglistapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("DELETE FROM tbl_Products")
    void deleteAllNotes();

    @Query("SELECT * FROM tbl_products ORDER BY name")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT PR.id, PR.name, PR.unit, (CASE WHEN MR.ProductId isnull THEN 0 else 1 end) 'Flag'  " +
            "FROM tbl_products 'PR'" +
            "LEFT JOIN tbl_Meals_Row 'MR' ON MR.ProductId = PR.id AND MR.MealId = :mealID" +
            " ORDER BY PR.name ")

    LiveData<List<Product>> getFlaggedProducts(int mealID);

    @Query("SELECT PR.id, PR.name, PR.unit, (CASE WHEN MR.ProductId isnull THEN 0 else 1 end) 'Flag'  " +
            "FROM tbl_products 'PR'" +
            "LEFT JOIN tbl_Meals_Row 'MR' ON MR.ProductId = PR.id AND MR.MealId = :mealID" +
            " WHERE LOWER(PR.name) LIKE LOWER('%'+ :phrase +'%') ORDER BY PR.name ")

    LiveData<List<Product>> getFlaggedFilteredProducts(int mealID, String phrase);



}
