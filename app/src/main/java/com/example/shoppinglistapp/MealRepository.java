package com.example.shoppinglistapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MealRepository {

    private MealDao mealDao;
    private MealRowDao mealRowDao;
    private LiveData<List<Meal>> allMeals;
    private LiveData<List<MealRow>> productsForMeal;

    long insertedMealId = -1;
    private CountDownLatch mLatch;


    public MealRepository(Application application)
    {
        ShoppingDatabase database = ShoppingDatabase.getInstance(application);
        mealDao = database.mealDao();
        allMeals = mealDao.getAllMeals();

        mealRowDao = database.mealRowDao();

    }
    public LiveData<List<Meal>> getAllMeals()
    {
        return allMeals;
    }

    public LiveData<Meal> getLastMeal() {return mealDao.getLastMeal();}

    public LiveData<List<MealRow>> getAllMealRows() {return mealRowDao.getAllMeals();}



    public long insert(Meal meal)
    {
        mLatch = new CountDownLatch(1);
        new MealRepository.InsertMealAsyncTask(mealDao).execute(meal);
        try
        {
            mLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return insertedMealId;
    }

    public void update(Meal meal)
    {
        new MealRepository.UpdateMealAsyncTask(mealDao).execute(meal);
    }

    public void delete(Meal meal)
    {
        new MealRepository.DeleteMealAsyncTask(mealDao).execute(meal);
    }




    public void deleteMeal(int mealID){new MealRepository.DeleteIdMealAsyncTask(mealDao).execute(mealID);}

    private static class DeleteIdMealAsyncTask extends AsyncTask<Integer, Void, Void>
    {
        private MealDao mealDao;

        private DeleteIdMealAsyncTask(MealDao mealDao)
        {
            this.mealDao = mealDao;
        }

        @Override
        protected Void doInBackground(Integer... mealID)
        {
            mealDao.deleteNotSavedMeal(mealID[0]);
            return null;
        }
    }

    public void deleteMealRow(int mealID){new MealRepository.DeleteIdMealRowAsyncTask(mealDao).execute(mealID);}

    private static class DeleteIdMealRowAsyncTask extends AsyncTask<Integer, Void, Void>
    {
        private MealDao mealDao;

        private DeleteIdMealRowAsyncTask(MealDao mealDao)
        {
            this.mealDao = mealDao;
        }

        @Override
        protected Void doInBackground(Integer... mealID)
        {

            mealDao.deleteNotSavedMealRows(mealID[0]);
            return null;
        }
    }



    private class InsertMealAsyncTask extends AsyncTask<Meal, Void, Void>
    {
        private MealDao mealDao;


        private InsertMealAsyncTask(MealDao mealDao)
        {
            this.mealDao = mealDao;
        }

        @Override
        protected Void doInBackground(Meal... meals)
        {
            insertedMealId = mealDao.insert(meals[0]);
            mLatch.countDown();
            return null;
        }



    }

    private static class UpdateMealAsyncTask extends AsyncTask<Meal, Void, Void>
    {
        private MealDao mealDao;

        private UpdateMealAsyncTask(MealDao mealDao)
        {
            this.mealDao = mealDao;
        }

        @Override
        protected Void doInBackground(Meal... meals)
        {
            mealDao.update(meals[0]);
            return null;
        }
    }

    private static class DeleteMealAsyncTask extends AsyncTask<Meal, Void, Void>
    {
        private MealDao mealDao;

        private DeleteMealAsyncTask(MealDao mealDao)
        {
            this.mealDao = mealDao;
        }

        @Override
        protected Void doInBackground(Meal... meals)
        {
            mealDao.delete(meals[0]);
            return null;
        }
    }





    public void setDeleteFlagAll(int flag)
    {
        new MealRepository.setDeleteFlagAllAsyncTask(mealRowDao).execute(flag);
    }


    private static class setDeleteFlagAllAsyncTask extends AsyncTask<Integer, Void, Void>
    {
        private MealRowDao mealRowDao;

        private setDeleteFlagAllAsyncTask(MealRowDao mealRowDao)
        {
            this.mealRowDao = mealRowDao;
        }

        @Override
        protected Void doInBackground(Integer... flag)
        {
            mealRowDao.setDeletedFlagAll(flag[0]);
            return null;
        }
    }



    public void setInsertFlagAll(int flag)
    {
        new MealRepository.setInsertFlagAllAsyncTask(mealRowDao).execute(flag);
    }


    private static class setInsertFlagAllAsyncTask extends AsyncTask<Integer, Void, Void>
    {
        private MealRowDao mealRowDao;

        private setInsertFlagAllAsyncTask(MealRowDao mealRowDao)
        {
            this.mealRowDao = mealRowDao;
        }

        @Override
        protected Void doInBackground(Integer... flag)
        {
            mealRowDao.setInsertedFlagAll(flag[0]);
            return null;
        }
    }

    public LiveData<List<MealRow>> getFlaggedMeals(int mealID) {return mealRowDao.getFlaggedMeals(mealID);}

    public LiveData<List<MealRow>> getProductsForMeal(int mealID) {return mealRowDao.getProductsForMeal(mealID);}
    public List<MealRow> getProductsForMealStatic (int mealID) {return mealRowDao.getProductsForMealStatic(mealID);}

    public LiveData<String> GetProductName(int mealID){return mealRowDao.getProductName(mealID);}

    public LiveData<String> GetProductUnit(int mealID){return mealRowDao.getProductUnit(mealID);}

    public long insertMealRow(MealRow mealRow)
    {

        mLatch = new CountDownLatch(1);
        new MealRepository.InsertMealRowAsyncTask(mealRowDao).execute(mealRow);
        try
        {
            mLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return insertedMealId;
    }

    public void updateMealRow(MealRow mealRow)
    {
        new MealRepository.UpdateMealRowAsyncTask(mealRowDao).execute(mealRow);
    }

    public void deleteMealRow(MealRow mealRow)
    {
        new MealRepository.DeleteMealRowAsyncTask(mealRowDao).execute(mealRow);
    }


    public void deleteDeleted(int mealID)
    {
        new MealRepository.DeleteDeletedMealAsyncTask(mealRowDao).execute(mealID);
    }

    public void deleteInserted(int mealID)
    {
        new MealRepository.DeleteInsertedMealAsyncTask(mealRowDao).execute(mealID);
    }


    public void updateInserted(MealRow mealRow)
    {
        new MealRepository.UpdateInsertedMealAsyncTask(mealRowDao).execute(mealRow);
    }

    public void updateDeleted(MealRow mealRow)
    {
        new MealRepository.UpdateDeletedMealAsyncTask(mealRowDao).execute(mealRow);
    }

    private static class UpdateDeletedMealAsyncTask extends AsyncTask<MealRow, Void, Void>
    {
        private MealRowDao mealRowDao;

        private UpdateDeletedMealAsyncTask(MealRowDao mealRowDao)
        {
            this.mealRowDao = mealRowDao;
        }

        @Override
        protected Void doInBackground(MealRow... mealRows)
        {
            mealRowDao.setDeletedFlagProd(mealRows[0].getDeleteFlag(),mealRows[0].getMealId(),mealRows[0].getId());
            return null;
        }
    }








    private static class UpdateInsertedMealAsyncTask extends AsyncTask<MealRow, Void, Void>
    {
        private MealRowDao mealRowDao;

        private UpdateInsertedMealAsyncTask(MealRowDao mealRowDao)
        {
            this.mealRowDao = mealRowDao;
        }

        @Override
        protected Void doInBackground(MealRow... mealRows)
        {
            mealRowDao.setInsertedFlagProd(mealRows[0].getInsertFlag(),mealRows[0].getMealId(),mealRows[0].getProductId());
            return null;
        }
    }







    private static class DeleteDeletedMealAsyncTask extends AsyncTask<Integer, Void, Void>
    {
        private MealRowDao mealRowDao;

        private DeleteDeletedMealAsyncTask(MealRowDao mealRowDao)
        {
            this.mealRowDao = mealRowDao;
        }

        @Override
        protected Void doInBackground(Integer... mealID)
        {
            mealRowDao.deleteDeleted(mealID[0]);
            return null;
        }
    }

    private static class DeleteInsertedMealAsyncTask extends AsyncTask<Integer, Void, Void>
    {
        private MealRowDao mealRowDao;

        private DeleteInsertedMealAsyncTask(MealRowDao mealRowDao)
        {
            this.mealRowDao = mealRowDao;
        }

        @Override
        protected Void doInBackground(Integer... mealID)
        {
            mealRowDao.deleteInserted(mealID[0]);
            return null;
        }
    }







    private class InsertMealRowAsyncTask extends AsyncTask<MealRow, Void, Void>
    {
        private MealRowDao mealRowDao;

        private InsertMealRowAsyncTask(MealRowDao mealRowDao)
        {
            this.mealRowDao = mealRowDao;
        }

        @Override
        protected Void doInBackground(MealRow... mealRows)
        {
            insertedMealId = mealRowDao.insert(mealRows[0]);
            mLatch.countDown();
            return null;
        }
    }

    private static class UpdateMealRowAsyncTask extends AsyncTask<MealRow, Void, Void>
    {
        private MealRowDao mealRowDao;

        private UpdateMealRowAsyncTask(MealRowDao mealRowDao)
        {
            this.mealRowDao = mealRowDao;
        }

        @Override
        protected Void doInBackground(MealRow... mealsRows)
        {
            mealRowDao.update(mealsRows[0]);
            return null;
        }
    }

    private static class DeleteMealRowAsyncTask extends AsyncTask<MealRow, Void, Void>
    {
        private MealRowDao mealRowDao;

        private DeleteMealRowAsyncTask(MealRowDao mealRowDao)
        {
            this.mealRowDao = mealRowDao;
        }

        @Override
        protected Void doInBackground(MealRow... mealRows)
        {
            mealRowDao.delete(mealRows[0]);
            return null;
        }
    }

}
