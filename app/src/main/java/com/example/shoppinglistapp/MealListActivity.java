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

public class MealListActivity extends AppCompatActivity {

    public static final int ADD_MEAL_REQUEST = 11;
    public static final int EDIT_MEAL_REQUEST = 12;

    public static final String MODE_ADD_SCHEDULE_MEAL = "com.example.shoppinglistapp.MODE_ADD_SCHEDULE_MEAL";
    public static final String EXTRA_MEAL_SCHEDULE_ID = "com.example.shoppinglistapp.EXTRA_MEAL_SCHEDULE_ID";
    public static final String EXTRA_MEAL_SCHEDULE_MEAL_VIEW = "com.example.shoppinglistapp.EXTRA_MEAL_SCHEDULE_MEAL_VIEW";

    public static final String EXTRA_INSERTED_MEAL_ID = "com.example.shoppinglistapp.EXTRA_INSERTED_MEAL_ID";
    EditText editTextSearchMeal;

    private MealScheduleRowViewModel mealScheduleRowViewModel;

    private MealViewModel mealViewModel;
    public MealAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FloatingActionButton buttonAddProduct = findViewById(R.id.button_add_meal);
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealListActivity.this, MealAddEditActivity.class);
                startActivityForResult(intent,ADD_MEAL_REQUEST);

            }


        });

        editTextSearchMeal = findViewById(R.id.edittext_search_meal);


        editTextSearchMeal.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                filter(editable.toString());
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view_meal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);



        adapter = new MealAdapter();
        recyclerView.setAdapter((adapter));

        mealScheduleRowViewModel = new ViewModelProvider(this
                ,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MealScheduleRowViewModel.class);


        mealViewModel = new ViewModelProvider(this
                ,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MealViewModel.class);

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


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction)
            {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        switch (which)
                        {
                            case DialogInterface.BUTTON_POSITIVE:

                                mealViewModel.delete(adapter.getMealAt(viewHolder.getAdapterPosition()));
                                Toast.makeText(MealListActivity.this, "Meal deleted", Toast.LENGTH_LONG).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                adapter.notifyDataSetChanged();
                                break;
                        }


                    }
                };

                TextView myMsg = new TextView(MealListActivity.this);
                myMsg.setText("\nSure you want to delete \n\n" + adapter.getMealAt(viewHolder.getAdapterPosition()).getMealName() + "?" );
                myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                myMsg.setTextColor(Color.BLACK);
                myMsg.setTextSize(15);

                AlertDialog.Builder builder = new AlertDialog.Builder(MealListActivity.this);
                builder.setView(myMsg).setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


            }
        }).attachToRecyclerView(recyclerView);

        Intent intent = getIntent();
        if(!intent.hasExtra(EXTRA_MEAL_SCHEDULE_ID))
        {
            adapter.setOnMealListener(new MealAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Meal meal) {
                    Intent intent = new Intent(MealListActivity.this, MealAddEditActivity.class);
                    intent.putExtra(MealAddEditActivity.EXTRA_MEAL_NAME,meal.getMealName());
                    intent.putExtra(MealAddEditActivity.EXTRA_MEAL_DESC,meal.getMealDescription());
                    intent.putExtra(MealAddEditActivity.EXTRA_MEAL_ID,meal.getId());

                    startActivityForResult(intent, EDIT_MEAL_REQUEST);

                }
            });
        }
        else // Wchodzenie z kontekstu dodawania posiłku do Schedule
        {
            adapter.setOnMeallongListener(new MealAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(Meal meal)
                {
                    Intent intent = new Intent(MealListActivity.this, MealAddEditActivity.class);
                    intent.putExtra(MealAddEditActivity.EXTRA_MEAL_NAME,meal.getMealName());
                    intent.putExtra(MealAddEditActivity.EXTRA_MEAL_DESC,meal.getMealDescription());
                    intent.putExtra(MealAddEditActivity.EXTRA_MEAL_ID,meal.getId());

                    startActivityForResult(intent, EDIT_MEAL_REQUEST);

                }
            });


            adapter.setOnMealListener(new MealAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Meal meal)
                {
                    //Toast.makeText(MealProductAddActivity.this, "test"+ product.getName(), Toast.LENGTH_SHORT).show();

                    int meal_ID = meal.getId();

                    Intent getIntent = getIntent();
                    int meal_schedule_id = getIntent.getIntExtra(EXTRA_MEAL_SCHEDULE_ID, 0);

                    Intent data = new Intent();
//                  data.putExtra(EXTRA_MEAL_SCHEDULE_ID, meal_ID);
                    setResult(RESULT_OK,data);

                    MealScheduleRow mealScheduleRow = new MealScheduleRow(meal_schedule_id,meal_ID,1);
                    mealScheduleRowViewModel.insert(mealScheduleRow);

                    finish();

                }
            });
        }
    }

    public void filter(String text)
    {

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

        Log.d("test", String.valueOf(adapter.getItemCount()));


        List<Meal> filteredMeals = new ArrayList<>();
        List<Meal> inputMeals = adapter.getMeals();
        Log.d("test", String.valueOf(adapter.getItemCount()));

        for (Meal meal : inputMeals)
        {
            if (meal.getMealName().toLowerCase().contains(text.toLowerCase()))
            {
                filteredMeals.add(meal);
            }
        }

        adapter.setMeals(filteredMeals);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        editTextSearchMeal = findViewById(R.id.edittext_search_meal);
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


        }


    }
}