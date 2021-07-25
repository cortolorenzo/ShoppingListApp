package com.example.shoppinglistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import static java.lang.Math.round;

public class WelcomeActivity extends AppCompatActivity {

    //private Handler mHandler;

    private long delay = 700;
    private int i = 0;

    int progress = 0;
    ProgressBar simpleProgressBar;
    TextView progressTxtView;

    int daysToSchedule = 1000;//3650;
    int daysScheduled = 0;

    Thread myThread;
    private CountDownLatch mLatch;

    MealScheduleRowViewModel mealScheduleRowViewModel;
    MealScheduleViewModel    mealScheduleViewModel;
    ProductViewModel productViewModel;
    MealViewModel mealViewModel;
    MealRowViewModel mealRowViewModel;

    String DBPath = "/data/user/0/com.example.shoppinglistapp/databases/shopping_database";


    SharedPreferences prefs = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        simpleProgressBar = (ProgressBar) findViewById(R.id.pgBar);
        progressTxtView = (TextView) findViewById(R.id.progressTxt);
        simpleProgressBar.setProgress(progress);
        mLatch = new CountDownLatch(1);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //ShoppingDatabase openDBState = ShoppingDatabase.instance;
        prefs = getSharedPreferences("com.example.shoppinglistapp", MODE_PRIVATE);

        if (prefs.getBoolean("firstrun", true))
        {
            Log.e("DB: ", "Not exist");

            mealScheduleViewModel = new ViewModelProvider(this
                    ,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                    .get(MealScheduleViewModel.class);

            productViewModel = new ViewModelProvider(this
                ,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(ProductViewModel.class);

            mealViewModel = new ViewModelProvider(this
                ,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MealViewModel.class);

            mealRowViewModel = new ViewModelProvider(this
                    ,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                    .get(MealRowViewModel.class);

            mealScheduleRowViewModel = new ViewModelProvider(this
                    ,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                    .get(MealScheduleRowViewModel.class);




            ShoppingDatabase db = ShoppingDatabase.getInstance(MyApplication.sInstance);
            db.query("select 1", null);

            //InsertProducts(db);
            //InsertMeals(db);
            //Progress Bar
            try
            {
                insertScheduleDateScope();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return;

        }

        MainActivityEnter();

        //Timer timer = new Timer();
        //timer.schedule(task, delay);



        /*Intent in = new Intent().setClass(WelcomeActivity.this,
                MainActivity.class).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        finish();*/

    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DBPath, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    void MainActivityEnter()
    {
        Intent in = new Intent().setClass(WelcomeActivity.this,
                MainActivity.class).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        finish();
    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        Log.e("DB Location: ", String.valueOf(dbFile));
        return dbFile.exists();
    }

    void insertScheduleDateScope() throws InterruptedException {

        final Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {

                int currProgress = 0;
                int fullProgress;

                ShoppingDatabase db = ShoppingDatabase.getInstance(MyApplication.sInstance);

                List<Product> productList = db.getProducts();
                List<Meal> mealList = db.getMeals();
                List<MealRow> mealRows = db.getMealRows();
                List<MealScheduleRow> mealScheduleRows = db.getMealScheduleRows();

                fullProgress = daysToSchedule + productList.size() + mealList.size() + mealRows.size() + mealScheduleRows.size();


                List<Date> DateScope = new ArrayList<>();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();

                Date date = new Date();
                c.setTime(date);
                c.add(Calendar.DATE, -365);
                date = c.getTime();
                mealScheduleViewModel.insert(new MealSchedule(date));



                daysScheduled = 1;

                while (daysScheduled < daysToSchedule )
                {
                    c.setTime(date);
                    c.add(Calendar.DATE, 1);

                    date = c.getTime();

                    final long czyInsert = mealScheduleViewModel.insert(new MealSchedule(date));
                    Log.d("Inser sukces: ", String.valueOf(czyInsert));

                    currProgress++;
                    progress = (int) Math.round(((double)currProgress/(double)fullProgress) * 100);
                    Log.d("Progress Bar: ", String.valueOf(progress));

                    SetProgressBar();
                    SetInfoText("Scheduled Meals Template", String.valueOf(czyInsert));

                    daysScheduled++;
                }

                for (Product product : productList)
                {
                    productViewModel.insert(product);
                    currProgress++;
                    progress = (int) Math.round(((double)currProgress/(double)fullProgress) * 100);
                    SetProgressBar();
                    SetInfoText("Products: ", String.valueOf(product.getName()));
                }

                for (Meal meal : mealList)
                {
                    mealViewModel.insert(meal);
                    currProgress++;
                    progress = (int) Math.round(((double)currProgress/(double)fullProgress) * 100);
                    SetProgressBar();
                    SetInfoText("Meals: ", String.valueOf(meal.getMealName()));
                }

                for (MealRow mealRow : mealRows)
                {
                    mealRowViewModel.insert(mealRow);
                    currProgress++;
                    progress = (int) Math.round(((double)currProgress/(double)fullProgress) * 100);
                    SetProgressBar();
                    SetInfoText("MealDefinition: ", String.valueOf(mealRow.getMealId()));
                }

                for (MealScheduleRow mealScheduleRow : mealScheduleRows)
                {
                    mealScheduleRowViewModel.insert(mealScheduleRow);
                    currProgress++;
                    progress = (int) Math.round(((double)currProgress/(double)fullProgress) * 100);
                    SetProgressBar();
                    SetInfoText("MealRowDefinition: ", String.valueOf(mealScheduleRow.getMealId()));
                }

                MainActivityEnter();

            }


        };

        myThread = new Thread(runnable);
        myThread.start();

    }


    void SetProgressBar()
    {
        simpleProgressBar.post(new Runnable()
        {
            @Override
            public void run()
            {
                simpleProgressBar.setProgress(progress);
            }


        });
    }

    void SetInfoText(final String entity, final String info)
    {
        progressTxtView.post(new Runnable()
        {
            @Override
            public void run()
            {
                progressTxtView.setText(entity + ": " + info);
            }

        });

    }


    TimerTask task = new TimerTask()
    {
        @Override
        public void run()
        {

            Intent in = new Intent().setClass(WelcomeActivity.this,
                    MainActivity.class).addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(in);
            finish();

        }

    };
}


