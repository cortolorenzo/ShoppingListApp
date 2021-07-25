package com.example.shoppinglistapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ShoppingListRepository
{
    long insertedShoppingListId = -1;
    private CountDownLatch mLatch;


    private ShoppingListDao shoppingListDao;

    public ShoppingListRepository(Application application)
    {
        ShoppingDatabase database = ShoppingDatabase.getInstance(application);
        shoppingListDao = database.shoppingListDao();

    }


    public LiveData<List<ShoppingList>> getShoppingLists()
    {
        return shoppingListDao.getAllShoppingLists();
    }


    public long insertShoppingList(ShoppingList shoppingList)
    {
        mLatch = new CountDownLatch(1);
        new ShoppingListRepository.InsertShoppingListAsyncTask(shoppingListDao).execute(shoppingList);
        try
        {
            mLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return insertedShoppingListId;
    }


    private class InsertShoppingListAsyncTask extends AsyncTask<ShoppingList, Void, Void>
    {
        private ShoppingListDao shoppingListDao;

        private InsertShoppingListAsyncTask(ShoppingListDao shoppingListDao)
        {
            this.shoppingListDao = shoppingListDao;
        }

        @Override
        protected Void doInBackground(ShoppingList... shoppingLists)
        {
            insertedShoppingListId = shoppingListDao.insert(shoppingLists[0]);
            mLatch.countDown();
            return null;
        }
    }


    public void delete(ShoppingList shoppingList)
    {
        new ShoppingListRepository.DeleteShoppingListAsyncTask(shoppingListDao).execute(shoppingList);
    }



    private static class DeleteShoppingListAsyncTask extends AsyncTask<ShoppingList, Void, Void>
    {
        private ShoppingListDao shoppingListDao;

        private DeleteShoppingListAsyncTask(ShoppingListDao shoppingListDao)
        {
            this.shoppingListDao = shoppingListDao;
        }

        @Override
        protected Void doInBackground(ShoppingList... shoppingLists)
        {
            shoppingListDao.delete(shoppingLists[0]);
            return null;
        }
    }










/// ROWS



    public LiveData<List<ShoppingListRow>> getAllShoppingListRows(  int listId)
    {
        return shoppingListDao.getAllShoppingListRows(listId);
    }

    public void insertShoppingListRow(ShoppingListRow shoppingListRow)
    {
        new ShoppingListRepository.InsertShoppingListRowAsyncTask(shoppingListDao).execute(shoppingListRow);
    }



    private static class InsertShoppingListRowAsyncTask extends AsyncTask<ShoppingListRow, Void, Void>
    {
        private ShoppingListDao shoppingListDao;

        private InsertShoppingListRowAsyncTask(ShoppingListDao shoppingListDao)
        {
            this.shoppingListDao = shoppingListDao;
        }

        @Override
        protected Void doInBackground(ShoppingListRow... shoppingListRows)
        {
            shoppingListDao.insert(shoppingListRows[0]);
            return null;
        }
    }



    public void update(ShoppingListRow shoppingListRow)
    {
        new ShoppingListRepository.UpdateShoppingListRowAsyncTask(shoppingListDao).execute(shoppingListRow);
    }

    private class UpdateShoppingListRowAsyncTask extends AsyncTask<ShoppingListRow, Void, Void>
    {
        private ShoppingListDao shoppingListDao;

        private UpdateShoppingListRowAsyncTask(ShoppingListDao shoppingListDao)
        {
            this.shoppingListDao = shoppingListDao;
        }

        @Override
        protected Void doInBackground(ShoppingListRow... shoppingListRows)
        {
            shoppingListDao.update(shoppingListRows[0]);
            return null;
        }
    }

    public void delete(ShoppingListRow shoppingListRow)
    {
        new ShoppingListRepository.DeleteShoppingListRowAsyncTask(shoppingListDao).execute(shoppingListRow);
    }



    private static class DeleteShoppingListRowAsyncTask extends AsyncTask<ShoppingListRow, Void, Void>
    {
        private ShoppingListDao shoppingListDao;

        private DeleteShoppingListRowAsyncTask(ShoppingListDao shoppingListDao)
        {
            this.shoppingListDao = shoppingListDao;
        }

        @Override
        protected Void doInBackground(ShoppingListRow... shoppingListRows)
        {
            shoppingListDao.delete(shoppingListRows[0]);
            return null;
        }
    }



}
