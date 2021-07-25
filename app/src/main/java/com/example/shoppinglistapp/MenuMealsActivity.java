package com.example.shoppinglistapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class MenuMealsActivity extends AppCompatActivity {

    public static final int CHECK_STATE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_menu_meals);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        CardView cv_products = findViewById(R.id.cv_Products);
        CardView cv_recipies = findViewById(R.id.cv_Recipies);

        cv_products.setCardBackgroundColor(getColor(R.color.whiteOpacity75));
        cv_recipies.setCardBackgroundColor(getColor(R.color.whiteOpacity75));


    }
    public void viewProducts (View v){

        CardView cardView = findViewById(R.id.cv_Products);
        cardView.setCardBackgroundColor(getColor(R.color.white));
        Intent intent = new Intent(MenuMealsActivity.this, ProductListActivity.class);
        startActivityForResult(intent,CHECK_STATE);

    }

    public void viewRecipies (View v){

        CardView cardView = findViewById(R.id.cv_Recipies);
        cardView.setCardBackgroundColor(getColor(R.color.white));
        Intent intent = new Intent(MenuMealsActivity.this, MealListActivity.class);
        startActivityForResult(intent,CHECK_STATE);

    }
}