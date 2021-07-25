package com.example.shoppinglistapp;
import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;


import java.util.List;

public class ShoppingListViewModel extends AndroidViewModel
{

    private ShoppingListRepository repository;

    public ShoppingListViewModel(@NonNull Application application)
    {
        super(application);
        repository = new ShoppingListRepository(application);
    }

    public void delete(ShoppingList shoppingList)
    {
        repository.delete(shoppingList);
    }



    public LiveData<List<ShoppingList>> getAllShoppingLists()
    {
        return repository.getShoppingLists();
    }

    public long insert(ShoppingList shoppingList)
    {
        return repository.insertShoppingList(shoppingList);
    }


// ROWS

    public void update(ShoppingListRow shoppingListRow)
    {
        repository.update(shoppingListRow);
    }

    public void insert(ShoppingListRow shoppingListRow)
    {
        repository.insertShoppingListRow(shoppingListRow);
    }

    public void delete(ShoppingListRow shoppingListRow)
    {
        repository.delete(shoppingListRow);
    }

    public LiveData<List<ShoppingListRow>> getAllShoppingListRows( int listId)
    {
        return repository.getAllShoppingListRows(listId);
    }



}
