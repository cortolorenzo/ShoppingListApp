package com.example.shoppinglistapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ProductRepository {

    private ProductDao productDao;
    private LiveData<List<Product>> allProducts;

    public ProductRepository(Application application)
    {
        ShoppingDatabase database = ShoppingDatabase.getInstance(application);
        productDao = database.productDao();
        allProducts = productDao.getAllProducts();

    }

    public void insert(Product product)
    {
        new InsertProductAsyncTask(productDao).execute(product);
    }

    public void update(Product product)
    {
        new UpdateProductAsyncTask(productDao).execute(product);
    }

    public void delete(Product product)
    {
        new DeleteProductAsyncTask(productDao).execute(product);
    }

    public void deleteAllNotes()
    {
        new DeleteAllProductAsyncTask(productDao).execute();
    }



    public LiveData<List<Product>> getAllProducts()
    {
        return allProducts;
    }
    public LiveData<List<Product>> getProductsFlagged(int mealID)
    {
        return productDao.getFlaggedProducts(mealID);
    }

    public LiveData<List<Product>> getFlaggedFilteredProducts(int mealID, String phrase)
    {
        return productDao.getFlaggedFilteredProducts(mealID, phrase);
    }

    private static class InsertProductAsyncTask extends AsyncTask<Product, Void, Void>
    {
        private ProductDao productDao;

        private InsertProductAsyncTask(ProductDao productDao)
        {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products)
        {
            productDao.insert(products[0]);
            return null;
        }
    }

    private static class UpdateProductAsyncTask extends AsyncTask<Product, Void, Void>
    {
        private ProductDao productDao;

        private UpdateProductAsyncTask(ProductDao productDao)
        {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products)
        {
            productDao.update(products[0]);
            return null;
        }
    }

    private static class DeleteProductAsyncTask extends AsyncTask<Product, Void, Void>
    {
        private ProductDao productDao;

        private DeleteProductAsyncTask(ProductDao productDao)
        {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products)
        {
            productDao.delete(products[0]);
            return null;
        }
    }

    private static class DeleteAllProductAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private ProductDao productDao;

        private DeleteAllProductAsyncTask(ProductDao productDao)
        {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            productDao.deleteAllNotes();
            return null;
        }
    }

}
