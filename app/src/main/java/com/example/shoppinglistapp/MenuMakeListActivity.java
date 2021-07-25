package com.example.shoppinglistapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import static com.example.shoppinglistapp.MainActivity.CHECK_STATE;



public class MenuMakeListActivity extends AppCompatActivity {

    CardView cardViewScheduler;
    CardView cardViewQuickList;

    public static final int ADD_QUICK_LIST_REQUEST = 31;


    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_make_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        cardViewScheduler = findViewById(R.id.cv_list_scheduler);
        cardViewQuickList = findViewById(R.id.cv_list_qck_list);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        cardViewScheduler.setCardBackgroundColor(getColor(R.color.whiteOpacity75));
        cardViewQuickList.setCardBackgroundColor(getColor(R.color.whiteOpacity75));

    }
    public void viewListScheduler (View v){


        cardViewScheduler.setCardBackgroundColor(getColor(R.color.white));
        Intent intent = new Intent(MenuMakeListActivity.this, ShoppingListSchedulerWizardActivity.class);
        startActivityForResult(intent,CHECK_STATE);

    }

    public void viewQuickList (View v){


        cardViewQuickList.setCardBackgroundColor(getColor(R.color.white));

        Intent intent = new Intent(MenuMakeListActivity.this, ShoppingListAddEditActivity.class);
        intent.putExtra(ShoppingListAddEditActivity.MODE_ADD_SHOPPING_lIST, 1); // 1 - add, 0 - edit
        intent.putExtra(ShoppingListAddEditActivity.MODE_SHOPPING_lIST_TYPE, 1); // 0 - Scheduler, 1 - Quick List

        startActivityForResult(intent, ADD_QUICK_LIST_REQUEST);


    }
}