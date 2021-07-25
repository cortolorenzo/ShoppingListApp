package com.example.shoppinglistapp;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;


import java.util.List;


public class ProductViewModel extends AndroidViewModel {
    private ProductRepository repository;
    private LiveData<List<Product>> allProducts;


    public ProductViewModel(@NonNull Application application)
    {
        super(application);
        repository = new ProductRepository(application);
        allProducts = repository.getAllProducts();

    }

    public void insert(Product product)
    {
        repository.insert(product);
    }

    public void update(Product product)
    {
        repository.update(product);
    }

    public void delete(Product product)
    {
        repository.delete(product);
    }

    public void deleteAllProducts(Product product)
    {
        repository.deleteAllNotes();
    }

    public LiveData<List<Product>> getAllProducts()
    {
        return allProducts;
    }
    public LiveData<List<Product>> getFlaggedProducts(int mealID)
    {
        return repository.getProductsFlagged(mealID);
    }

    public LiveData<List<Product>> getFlaggedFilteredProducts(int mealID, String phrase)
    {
        return repository.getFlaggedFilteredProducts(mealID, phrase);
    }





}
