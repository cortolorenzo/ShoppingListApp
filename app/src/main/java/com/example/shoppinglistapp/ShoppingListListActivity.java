package com.example.shoppinglistapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListListActivity extends AppCompatActivity {

    public static final int ADD_LIST_REQUEST = 11;
    public static final int PREVIEW_LIST_REQUEST = 12;

    EditText editTextSearchList;
    RecyclerView recyclerView;

    private ShoppingListViewModel shoppingListViewModel;
    public ShoppingListListAdapter adapter;

    @Override
    public void onBackPressed()
    {
        finish();
      /*  Intent intent = new Intent(ShoppingListListActivity.this,MainActivity.class);
        startActivity(intent);*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FloatingActionButton buttonAddProduct = findViewById(R.id.button_add_list);
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingListListActivity.this, MenuMakeListActivity.class);
                startActivityForResult(intent, ADD_LIST_REQUEST);

            }


        });

        editTextSearchList = findViewById(R.id.edittext_search_list);


        editTextSearchList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        recyclerView = findViewById(R.id.recycler_view_sList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        adapter = new ShoppingListListAdapter();
        recyclerView.setAdapter((adapter));

        shoppingListViewModel = new ViewModelProvider(this
                , ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(ShoppingListViewModel.class);


        shoppingListViewModel.getAllShoppingLists().observe(this, new Observer<List<ShoppingList>>() {
            @Override
            public void onChanged(List<ShoppingList> shoppingLists) {
                adapter.setShoppingList(shoppingLists);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                shoppingListViewModel.delete(adapter.getShoppingListAt(viewHolder.getAdapterPosition()));
                                Toast.makeText(ShoppingListListActivity.this, "Meal deleted", Toast.LENGTH_LONG).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                adapter.notifyDataSetChanged();
                                break;
                        }


                    }
                };

                TextView myMsg = new TextView(ShoppingListListActivity.this);
                myMsg.setText("\nSure you want to delete \n\n" + adapter.getShoppingListAt(viewHolder.getAdapterPosition()).getTitle() + "?");
                myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                myMsg.setTextColor(Color.BLACK);
                myMsg.setTextSize(15);

                AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingListListActivity.this);
                builder.setView(myMsg).setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


            }
        }).attachToRecyclerView(recyclerView);


        adapter.setOnMealListener(new ShoppingListListAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(ShoppingList shoppingList) {
                Intent intent = new Intent(ShoppingListListActivity.this, ShoppingListDetailsActivity.class);
                intent.putExtra(ShoppingListDetailsActivity.SHOPPING_LIST_ID, shoppingList.getId());
                intent.putExtra(ShoppingListDetailsActivity.MODE_SHOPPING_lIST_TYPE, shoppingList.getType()); // 0 - Scheduler, 1 - Quick List

                startActivityForResult(intent, PREVIEW_LIST_REQUEST);

            }
        });

    }
    public void filter(String text)
    {

        shoppingListViewModel.getAllShoppingLists().observe(this, new Observer<List<ShoppingList>>()
        {
            @Override
            public void onChanged(List<ShoppingList> shoppingLists)
            {
                //update RecyclerView
                adapter.setShoppingList(shoppingLists);
                //Toast.makeText(ProductListActivity.this, "Use + to add a new product",Toast.LENGTH_LONG).show();
            }
        });

        Log.d("test", String.valueOf(adapter.getItemCount()));

        List<ShoppingList> filteredShoppingLists = new ArrayList<>();
        List<ShoppingList> inputShoppingLists = adapter.getShoppingLists();
        Log.d("test", String.valueOf(adapter.getItemCount()));

        for (ShoppingList shoppingList : adapter.getShoppingLists())
        {
            if (shoppingList.getTitle().toLowerCase().contains(text.toLowerCase()))
            {
                filteredShoppingLists.add(shoppingList);
            }
        }

        adapter.setShoppingList(filteredShoppingLists);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        /*editTextSearchMeal = findViewById(R.id.edittext_search_meal);
        int insertedMeal = data.getIntExtra(MealAddEditActivity.EXTRA_INSERTED_MEAL_ID,0);

        if(requestCode == ADD_MEAL_REQUEST && resultCode == RESULT_CANCELED && insertedMeal == 0)
        {

        }

        Log.d("Result inserted id ", String.valueOf(insertedMeal));
        if(requestCode == ADD_MEAL_REQUEST && resultCode == RESULT_OK && insertedMeal == 0)
        {
            String mealName = data.getStringExtra(MealAddEditActivity.EXTRA_MEAL_NAME);
            String mealDesc = data.getStringExtra(MealAddEditActivity.EXTRA_MEAL_DESC);

            Meal meal = new Meal(mealName,mealDesc);
            mealViewModel.insert(meal);

            Toast.makeText(this,"Meal added", Toast.LENGTH_SHORT).show();
            editTextSearchMeal.setText("");
            return;


        }
        else if (requestCode == ADD_MEAL_REQUEST && resultCode == RESULT_OK && insertedMeal != 0)
        {
            Toast.makeText(this,"Meal added", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(requestCode == EDIT_MEAL_REQUEST && resultCode == RESULT_CANCELED)
        {

            int id = data.getIntExtra(MealAddEditActivity.EXTRA_MEAL_ID, -1);
            if(id == -1)
            {
                Toast.makeText(this,"Meal can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            mealViewModel.deleteInserted(id);
            mealViewModel.setDeleteFlagAll(0);

            if (data.getBooleanExtra(MealAddEditActivity.EXTRA_IS_UPDATED, false))
            {
                Toast.makeText(this,"Changes not saved", Toast.LENGTH_SHORT).show();
            }


        }

        else if(requestCode == EDIT_MEAL_REQUEST && resultCode == RESULT_OK)
        {
            int id = data.getIntExtra(MealAddEditActivity.EXTRA_MEAL_ID, -1);

            if(id == -1)
            {
                Toast.makeText(this,"Meal can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String mealName = data.getStringExtra(MealAddEditActivity.EXTRA_MEAL_NAME);
            String mealDesc = data.getStringExtra(MealAddEditActivity.EXTRA_MEAL_DESC);

            Meal meal = new Meal(mealName,mealDesc);
            meal.setId(id);
            mealViewModel.update(meal);

            mealViewModel.setInsertFlagAll(0);
            mealViewModel.deleteDeleted(id);

            if (data.getBooleanExtra(MealAddEditActivity.EXTRA_IS_UPDATED, false))
            {
                Toast.makeText(this,"Meal updated",Toast.LENGTH_SHORT).show();
            }

            editTextSearchMeal.setText("");
        }
        else
        {
            if(requestCode == ADD_MEAL_REQUEST && insertedMeal != 0){
                mealViewModel.deleteMeal(insertedMeal);
                mealViewModel.deleteMealRow(insertedMeal);
                Toast.makeText(this,"Meal not added", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (requestCode == ADD_MEAL_REQUEST)
            {
                Toast.makeText(this,"Meal not added", Toast.LENGTH_SHORT).show();
            }


        }*/


    }
}