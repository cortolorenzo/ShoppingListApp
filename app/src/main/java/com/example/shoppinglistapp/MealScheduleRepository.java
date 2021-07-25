package com.example.shoppinglistapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MealScheduleRepository
{

    private MealScheduleDao mealScheduleDao;

    private LiveData<List<MealSchedule>> allSchedules;
    private LiveData<List<MealScheduleRow>> mealsPerSchedule;

    long insertedMealScheduleId = -1;
    private CountDownLatch mLatch;

    public MealScheduleRepository(Application application)
    {
        ShoppingDatabase database = ShoppingDatabase.getInstance(application);
        mealScheduleDao = database.mealScheduleDao();
       // allSchedules = mealScheduleDao.getAllMealSchedules(long startDate, long endDate);
    }


    public LiveData<List<MealSchedule>> getAllMealSchedules(java.util.Date startDate, java.util.Date endDate)
    {
        return mealScheduleDao.getAllMealSchedules( startDate,  endDate);
    }


    public LiveData<List<MealScheduleRow>> getMealScheduleRows(int mealScheduleID)
    {
        return mealScheduleDao.getMealScheduleRows(mealScheduleID);
    }

    public LiveData<List<MealScheduleRow>> getMealsForSchedule ()
    {
        return mealScheduleDao.getMealsForSchedule();
    }






    public long insert(MealSchedule mealSchedule)
    {
        mLatch = new CountDownLatch(1);
        new InsertMealScheduleAsyncTask(mealScheduleDao).execute(mealSchedule);
        try
        {
            mLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return insertedMealScheduleId;
    }

    private class InsertMealScheduleAsyncTask extends AsyncTask<MealSchedule, Void, Void>
    {
        private MealScheduleDao mealScheduleDao;

        private InsertMealScheduleAsyncTask(MealScheduleDao mealScheduleDao)
        {
            this.mealScheduleDao = mealScheduleDao;
        }

        @Override
        protected Void doInBackground(MealSchedule... mealSchedules)
        {
            insertedMealScheduleId = mealScheduleDao.insert(mealSchedules[0]);
            mLatch.countDown();
            return null;
        }
    }


    public long insert(MealScheduleRow mealScheduleRow)
    {
        mLatch = new CountDownLatch(1);
        new InsertMealScheduleRowAsyncTask(mealScheduleDao).execute(mealScheduleRow);
        try
        {
            mLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return insertedMealScheduleId;
    }

    private class InsertMealScheduleRowAsyncTask extends AsyncTask<MealScheduleRow, Void, Void>
    {
        private MealScheduleDao mealScheduleDao;

        private InsertMealScheduleRowAsyncTask(MealScheduleDao mealScheduleDao)
        {
            this.mealScheduleDao = mealScheduleDao;
        }

        @Override
        protected Void doInBackground(MealScheduleRow... mealScheduleRows)
        {
            insertedMealScheduleId = mealScheduleDao.insert(mealScheduleRows[0]);
            mLatch.countDown();
            return null;
        }
    }



    public void deleteMealScheduleRow(MealScheduleRow mealScheduleRow)
    {
        new MealScheduleRepository.DeleteMealScheduleRowAsyncTask(mealScheduleDao).execute(mealScheduleRow);
    }

    private static class DeleteMealScheduleRowAsyncTask extends AsyncTask<MealScheduleRow, Void, Void>
    {
        private MealScheduleDao mealScheduleDao;

        private DeleteMealScheduleRowAsyncTask(MealScheduleDao mealScheduleDao)
        {
            this.mealScheduleDao = mealScheduleDao;
        }

        @Override
        protected Void doInBackground(MealScheduleRow... mealScheduleRows)
        {
            mealScheduleDao.delete(mealScheduleRows[0]);
            return null;
        }
    }


    public void updateMealScheduleRow(MealScheduleRow mealScheduleRow)
    {
        new MealScheduleRepository.UpdateMealScheduleRowAsyncTask(mealScheduleDao).execute(mealScheduleRow);
    }

    private static class UpdateMealScheduleRowAsyncTask extends AsyncTask<MealScheduleRow, Void, Void>
    {
        private MealScheduleDao mealScheduleDao;

        private UpdateMealScheduleRowAsyncTask(MealScheduleDao mealScheduleDao)
        {
            this.mealScheduleDao = mealScheduleDao;
        }

        @Override
        protected Void doInBackground(MealScheduleRow... mealScheduleRows)
        {
            mealScheduleDao.update(mealScheduleRows[0]);
            return null;
        }
    }


    public void updateMealSchedule(MealSchedule mealSchedule)
    {
        new MealScheduleRepository.UpdateMealScheduleAsyncTask(mealScheduleDao).execute(mealSchedule);
    }

    private static class UpdateMealScheduleAsyncTask extends AsyncTask<MealSchedule, Void, Void>
    {
        private MealScheduleDao mealScheduleDao;

        private UpdateMealScheduleAsyncTask(MealScheduleDao mealScheduleDao)
        {
            this.mealScheduleDao = mealScheduleDao;
        }

        @Override
        protected Void doInBackground(MealSchedule... mealSchedules)
        {
            mealScheduleDao.update(mealSchedules[0]);
            return null;
        }
    }


}
