package com.example.shoppinglistapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShoppingListDao
{

    @Insert
    long insert(ShoppingList shoppingList);

    @Update
    void update(ShoppingList shoppingList);

    @Delete
    void delete(ShoppingList shoppingList);


    @Query("SELECT * FROM tbl_ShoppingList ORDER BY id desc")
    LiveData<List<ShoppingList>> getAllShoppingLists();

// Rows

    @Insert
    void insert(ShoppingListRow shoppingListRow);

    @Update
    void update(ShoppingListRow shoppingListRow);

    @Delete
    void delete(ShoppingListRow shoppingListRow);


    @Query("SELECT slr.id, slr.shoppingListId, slr.productId, slr.flag, slr.isBought, slr.qnt, slr.tmpQnt, p.name as 'productName', p.unit as 'Units' " +
            "FROM tbl_ShoppingListRow slr" +
            " INNER JOIN tbl_products p ON p.id = slr.productId" +
            " WHERE slr.shoppingListId = :listId" +
            " ORDER BY slr.id asc")
    LiveData<List<ShoppingListRow>> getAllShoppingListRows( int listId);



}
