package com.example.shoppinglistapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import  java.time.LocalTime;

@Database(

        entities =
                {

                Product.class

                , Meal.class
                , MealRow.class

                , MealSchedule.class
                , MealScheduleRow.class

                , ShoppingList.class
                , ShoppingListRow.class

        }
        , version = 4)

@TypeConverters({Converters.class})
public abstract class ShoppingDatabase extends RoomDatabase
{

        // static instance so that it can be accessed from an place in our app
    public static ShoppingDatabase instance;
    public int daysToSchedule = 3650;
    public int daysScheduled = 0;

    //abstract method because it doesn't provide method body
    public abstract ProductDao productDao();
    public abstract MealDao mealDao();
    public abstract MealRowDao mealRowDao();
    public abstract MealScheduleDao mealScheduleDao();
    public abstract ShoppingListDao shoppingListDao();

    // synchronized means that only one thread can access this method at a time
    public static synchronized ShoppingDatabase getInstance(Context context)
    {

        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), ShoppingDatabase.class, "shopping_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db)
        {
            super.onCreate(db);
            //new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>
    {
        private ProductDao productDao;
        private MealDao mealDao;
        private MealRowDao mealRowDao;
        private MealScheduleDao mealScheduleDao;

        private int daysToScheduleAsync;
        private int daysScheduledAsync;

        ShoppingDatabase dbAsync;


        private PopulateDbAsyncTask(ShoppingDatabase db)
        {
            productDao = db.productDao();
            mealDao = db.mealDao();
            mealRowDao = db.mealRowDao();
            mealScheduleDao = db.mealScheduleDao();


        }




        @Override
        protected Void doInBackground(Void... voids)
        {


            mealDao.insert(new Meal("Tofucznicowa rozkosz", "To jest przepis jak zrobić tofucznicową rozkosz"));
            mealDao.insert(new Meal("Jajecznica", "To jest przepis jak zrobić Jajecznicę"));
            mealDao.insert(new Meal("Omlet", "To jest przepis jak zrobić Omlet"));


            mealRowDao.insert(new MealRow(1, 1,2));
            mealRowDao.insert(new MealRow(1, 2,2));
            mealRowDao.insert(new MealRow(1, 3,6));
            mealRowDao.insert(new MealRow(1, 4,3));
            mealRowDao.insert(new MealRow(1, 9,6));
            mealRowDao.insert(new MealRow(1, 10,3));
            mealRowDao.insert(new MealRow(1, 11,1));


            mealRowDao.insert(new MealRow(2, 8,1));
            mealRowDao.insert(new MealRow(2, 9,6));
            mealRowDao.insert(new MealRow(2, 10,3));
            mealRowDao.insert(new MealRow(2, 11,1));

            mealRowDao.insert(new MealRow(3, 1,2));
            mealRowDao.insert(new MealRow(3, 2,2));
            mealRowDao.insert(new MealRow(3, 3,6));
            mealRowDao.insert(new MealRow(3, 4,3));
            mealRowDao.insert(new MealRow(3, 5,13));

            return null;
        }
    }



        List<Product> products = new ArrayList<>();
        List<Meal> meals = new ArrayList<>();
        List<MealRow> mealRows = new ArrayList<>();
        List<MealScheduleRow> mealScheduleRows = new ArrayList<>();


        List<Product> getProducts()
        {

            products.add(new Product("Egg", "pcs."));
            products.add(new Product("Flour", "g"));
            products.add(new Product("Milk", "l"));
            products.add(new Product("Banana", "pcs."));
            products.add(new Product("Apple", "pcs."));
            products.add(new Product("Cinnamon", "g"));
            products.add(new Product("Jam", "g"));
            products.add(new Product("Ham", "g"));
            products.add(new Product("Cheese", "g"));
            products.add(new Product("Ketchup", "ml"));
            products.add(new Product("Olives", "g"));
            products.add(new Product("Spinach", "g"));
            products.add(new Product("Tomato", "g"));
            products.add(new Product("Pepper", "g"));
            products.add(new Product("Salt", "g"));
            products.add(new Product("Tofu", "g"));
            products.add(new Product("Coconut oil", "ml"));
            products.add(new Product("Onion", "pcs."));
            products.add(new Product("Mushrooms", "pcs."));
            products.add(new Product("Tomato paste", "l"));
            products.add(new Product("Water", "l"));
            products.add(new Product("Bread", "g"));
            products.add(new Product("Buns", "pcs."));
            products.add(new Product("Chive", "pcs."));
            products.add(new Product("Rice", "g"));
            products.add(new Product("Pasta", "g"));
            products.add(new Product("Buckwheat", "g"));



            return products;
        }

        List<MealScheduleRow> getMealScheduleRows()
        {

            mealScheduleRows.add(new MealScheduleRow(365,3,4));
            mealScheduleRows.add(new MealScheduleRow(365,2,1));
            mealScheduleRows.add(new MealScheduleRow(365,1,2));

            mealScheduleRows.add(new MealScheduleRow(366,1,1));
            mealScheduleRows.add(new MealScheduleRow(366,2,4));
            mealScheduleRows.add(new MealScheduleRow(366,3,2));

            return mealScheduleRows;
        }

        List<Meal> getMeals()
        {
            meals.add(new Meal("Omelet", "Omelet with fresh fruits"));
            meals.add(new Meal("Tofu's delight", "Tofu with vegetables"));
            meals.add(new Meal("Scrambled eggs", "Perfect breakfast! ;)"));

            return meals;
        }

        List<MealRow> getMealRows()
        {
            //Omelet
            mealRows.add(new MealRow(1, 1,1));
            mealRows.add(new MealRow(1, 2,80));
            mealRows.add(new MealRow(1, 3,0.150));
            mealRows.add(new MealRow(1, 4,1));
            mealRows.add(new MealRow(1, 5,0.5));
            mealRows.add(new MealRow(1, 6,2));
            mealRows.add(new MealRow(1, 7,10));

            //Tofu's delight
            mealRows.add(new MealRow(2, 18,1));
            mealRows.add(new MealRow(2, 19,4));
            mealRows.add(new MealRow(2, 16,125));
            mealRows.add(new MealRow(2, 12,50));
            mealRows.add(new MealRow(2, 11,15));
            mealRows.add(new MealRow(2, 20,0.5));
            mealRows.add(new MealRow(2, 25,100));

            //Scrambled eggs
            mealRows.add(new MealRow(3, 1,4));
            mealRows.add(new MealRow(3, 18,1));
            mealRows.add(new MealRow(3, 19,3));
            mealRows.add(new MealRow(3, 24,2));
            mealRows.add(new MealRow(3, 23,2));
            mealRows.add(new MealRow(3, 8,10));
            mealRows.add(new MealRow(3, 11,15));


            return mealRows;
        }



}
