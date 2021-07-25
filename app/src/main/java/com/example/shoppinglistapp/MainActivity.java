package com.example.shoppinglistapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final int CHECK_STATE = 1;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        prefs = getSharedPreferences("com.example.shoppinglistapp", MODE_PRIVATE);
        prefs.edit().putBoolean("firstrun", false).commit();

    }
    @Override
    public void onBackPressed()
    {
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        CardView cv_szeduler = findViewById(R.id.cv_scheduler);
        CardView cv_make_list = findViewById(R.id.cv_makelist);
        CardView cv_meals = findViewById(R.id.cv_meals);
        CardView cv_lists = findViewById(R.id.cv_lists);


        cv_szeduler.setCardBackgroundColor(getColor(R.color.whiteOpacity75));
        cv_make_list.setCardBackgroundColor(getColor(R.color.whiteOpacity75));
        cv_meals.setCardBackgroundColor(getColor(R.color.whiteOpacity75));
        cv_lists.setCardBackgroundColor(getColor(R.color.whiteOpacity75));


    }

    public void viewSchedule(View v){

        CardView cardView = findViewById(R.id.cv_scheduler);
        cardView.setCardBackgroundColor(getColor(R.color.white));
        Intent intent = new Intent(MainActivity.this, MealScheduleActivity.class);
        startActivityForResult(intent,CHECK_STATE);

    }

    public void viewMakeList (View v){

        CardView cardView = findViewById(R.id.cv_makelist);
        cardView.setCardBackgroundColor(getColor(R.color.white));
        Intent intent = new Intent(MainActivity.this, MenuMakeListActivity.class);
        startActivityForResult(intent,CHECK_STATE);

    }

    public void viewMeals (View v){

        CardView cardView = findViewById(R.id.cv_meals);
        cardView.setCardBackgroundColor(getColor(R.color.white));
        Intent intent = new Intent(MainActivity.this, MenuMealsActivity.class);
        startActivityForResult(intent,CHECK_STATE);

    }

    public void viewLists (View v){

        CardView cardView = findViewById(R.id.cv_lists);
        cardView.setCardBackgroundColor(getColor(R.color.white));
        Intent intent = new Intent(MainActivity.this, ShoppingListListActivity.class);
        startActivityForResult(intent,CHECK_STATE);

    }


}