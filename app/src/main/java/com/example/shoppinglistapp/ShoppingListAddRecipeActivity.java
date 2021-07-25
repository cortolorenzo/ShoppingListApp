package com.example.shoppinglistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import android.os.Bundle;

public class ShoppingListAddRecipeActivity extends AppCompatActivity {


    public MealViewModel mealViewModel;
    private ShoppingListAddRecipeAdapter adapter;
    private ShoppingListViewModel shoppingListViewModel;
    public MealRowViewModel mealRowViewModel;


    public Handler headerHandler = new Handler();


   private int iMealId = 0;
    List<MealRow> iMealProducts;
    List<ShoppingListRow> iListFromShoppingList;
    private int iMealQnt;

    public static final String EXTRA_LIST_ID =
            "com.example.shoppinglistapp.EXTRA_LIST_ID";

    boolean czyEdit;
    private int listId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_add_recipe);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("Choose meal to add");

        Intent intent = getIntent();
        listId = intent.getIntExtra(EXTRA_LIST_ID, 0);
        Log.e("edit mode", String.valueOf(listId));
        if( listId == -1)
        {
            czyEdit = false;
        }
        else
        {
            czyEdit = true;
        }

        RecyclerView recyclerView = findViewById(R.id.rv_shopping_list_add_recipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mealViewModel = new ViewModelProvider(this
                ,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MealViewModel.class);

        shoppingListViewModel = new ViewModelProvider(this
                , ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(ShoppingListViewModel.class);

        mealRowViewModel = new ViewModelProvider(this
                , ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MealRowViewModel.class);


        adapter = new ShoppingListAddRecipeAdapter();
        adapter.shoppingListAddRecipeActivity = this;
        recyclerView.setAdapter(adapter);


        mealViewModel.getAllMeals().observe(this, new Observer<List<Meal>>()
        {
            @Override
            public void onChanged(List<Meal> meals)
            {
                //update RecyclerView

                adapter.setMeals(meals);
                //Toast.makeText(ProductListActivity.this, "Use + to add a new product",Toast.LENGTH_LONG).show();
            }
        });

        headerHandler.removeCallbacks(UpdateQnt);
        headerHandler.postDelayed(UpdateQnt, 100);




        adapter.setOnMealListener(new ShoppingListAddRecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Meal meal)
            {
                //Toast.makeText(MealProductAddActivity.this, "test"+ product.getName(), Toast.LENGTH_SHORT).show();

                iMealId = meal.getId();
                iMealQnt = meal.getQnt();


                Intent i = getIntent();
                iListFromShoppingList = (List<ShoppingListRow>) i.getSerializableExtra("LIST");

           /*     for(ShoppingListRow shoppingListRow : (List<ShoppingListRow>) i.getSerializableExtra("LIST"))
                {
                    if (shoppingListRow.getProductId() == id_product)
                    {
                        Toast.makeText(ShoppingListAddProductActivity.this, "Product already on a list", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                */

                mealRowViewModel.getProductsForMeal(iMealId).observe(adapter.shoppingListAddRecipeActivity, new Observer<List<MealRow>>()
                {
                    @Override
                    public void onChanged(List<MealRow> mealRows)
                    {
                        adapter.setMealProducts(mealRows);
                    }});

                headerHandler.removeCallbacks(SaveRecipe);
                headerHandler.postDelayed(SaveRecipe, 100);

                //iMealRows = adapter.getProductsForMeal(mealId);



               /* for(ShoppingListRow shoppingListRow : (List<ShoppingListRow>) i.getSerializableExtra("LIST"))
                {
                    if (shoppingListRow.getProductId() == id_product)
                    {
                        Toast.makeText(ShoppingListAddProductActivity.this, "Product already on a list", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Intent data = new Intent();
                //data.putExtra(EXTRA_PRODUCT_ID, id_product);
                Log.d("mealID ", String.valueOf(listId));

                Log.d("czyedit ", String.valueOf(czyEdit));

                setResult(RESULT_OK,data);

                if (czyEdit == false)
                {
                    ShoppingListRow shoppingListRow = new ShoppingListRow(-1,id_product,1,0,1);
                    shoppingListViewModel.insert(shoppingListRow);

                }
                else
                {
                    ShoppingListRow shoppingListRow = new ShoppingListRow(listId,id_product,1,0,1);
                    shoppingListViewModel.insert(shoppingListRow);
                }


*/



                //finish();


            }
        });



    }

    private Runnable SaveRecipe = new Runnable() {
        @Override
        public void run() {


            iMealProducts = adapter.mealRows;

            List<MealRow> productsToUpdate = new ArrayList<MealRow>();
            List<MealRow> productsToAdd = new ArrayList<MealRow>();
            boolean isNestedMatched;

// List gathering for update and for insert
            OUTER_LOOP: for(MealRow mealProduct : iMealProducts)
            {
                Log.e("List size source", String.valueOf(iListFromShoppingList.size()));
                if(iListFromShoppingList.size() == 0)
                {
                    productsToAdd.add(mealProduct);
                    continue;
                }

                for(ShoppingListRow shoppingListRow : iListFromShoppingList)
                {

                    if (shoppingListRow.getProductId() == mealProduct.getProductId())
                    {
                        productsToUpdate.add(mealProduct);
                        continue OUTER_LOOP;
                    }
                }
                productsToAdd.add(mealProduct);

            }

            Log.e("ToAdd", String.valueOf(productsToAdd.size()));
            Log.e("ToUpdate", String.valueOf(productsToUpdate.size()));


// Edit mode or new list

                for(MealRow productToAdd : productsToAdd)
                {
                    double qnt = productToAdd.getQuantity() * iMealQnt;
                    ShoppingListRow shoppingListRow = new ShoppingListRow(listId,productToAdd.getProductId(),qnt,0,1);

                    shoppingListRow.setTmpQnt(qnt);
                    shoppingListViewModel.insert(shoppingListRow);

                }

                for(MealRow productToUpdate : productsToUpdate)
                {
                    for(ShoppingListRow shoppingListRow : iListFromShoppingList)
                    {
                        if(shoppingListRow.getProductId() == productToUpdate.getProductId())
                        {
                            if (czyEdit)
                            {
                                shoppingListRow.setTmpQnt(shoppingListRow.getTmpQnt() + (iMealQnt*productToUpdate.getQuantity()));
                            }
                            else
                            {
                                shoppingListRow.setQnt(shoppingListRow.getQnt() + (iMealQnt*productToUpdate.getQuantity()));
                            }

                            shoppingListViewModel.update(shoppingListRow);
                        }
                    }

                }



            Log.e("test", String.valueOf(iMealId));
            for (MealRow  mealRow : iMealProducts)
            {
                Log.e("test", mealRow.getProductName());
            }
            finish();

        }
    };

    private Runnable UpdateQnt = new Runnable()
    {
        @Override
        public void run()
        {
            for(Meal meal : adapter.getMeals())
            {
                meal.setQnt(1);
                mealViewModel.update(meal);
                adapter.notifyDataSetChanged();
            }
        }



    };



}