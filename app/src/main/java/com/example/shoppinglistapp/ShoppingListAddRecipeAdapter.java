package com.example.shoppinglistapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShoppingListAddRecipeAdapter extends RecyclerView.Adapter<ShoppingListAddRecipeAdapter.ShoppingListAddRecipeHolder>
{

    Context context;
    private Meal meal;
    private List<Meal> meals = new ArrayList<>();
    public List<MealRow> mealRows = new ArrayList<>();

    private ShoppingListAddRecipeAdapter.OnItemClickListener listener;
    private ShoppingListAddRecipeAdapter.OnItemLongClickListener listenerLong;
    public MealScheduleActivity mealScheduleActivity;

    public ShoppingListAddRecipeActivity shoppingListAddRecipeActivity;
    public MealScheduleRowViewModel mealScheduleRowViewModel;




    SharedPreferences prefs;
    List<CardView> cardViews = new ArrayList<>();
    public MealRowViewModel mealRowViewModel;
    public MealScheduleAdapter adapter;
    private int lastPosition = -1;
    CardView selectedCard;

    @NonNull
    @Override
    public ShoppingListAddRecipeAdapter.ShoppingListAddRecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_item_schedule, parent,false);





        return new ShoppingListAddRecipeAdapter.ShoppingListAddRecipeHolder((itemView));


    }


    @Override
    public void onBindViewHolder(@NonNull final ShoppingListAddRecipeAdapter.ShoppingListAddRecipeHolder holder, final int position)
    {
        final Meal currentMeal = meals.get(position);
        holder.textViewTitle.setText(currentMeal.getMealName());
        holder.TextViewQnt.setText(String.valueOf(currentMeal.getQnt()));


        holder.arrowInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int prevQnt = currentMeal.getQnt();
                int newQnt = prevQnt + 1;

                currentMeal.setQnt(newQnt);
                shoppingListAddRecipeActivity.mealViewModel.update(currentMeal);
            }
        });


        holder.arrowDecr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                int newQnt;

                int prevQnt = currentMeal.getQnt();
                if (prevQnt == 1 || prevQnt == 0)
                {
                    newQnt = 0;
                }
                else
                {
                    newQnt= prevQnt - 1;
                }
                currentMeal.setQnt(newQnt);
                shoppingListAddRecipeActivity.mealViewModel.update(currentMeal);
            }
        });


        /*holder.cardViewSchedule.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        switch (which)
                        {
                            case DialogInterface.BUTTON_POSITIVE:

                                mealScheduleActivity.mealScheduleRowViewModel.deleteMealScheduleRow(currentMeal);
                                Toast.makeText(mealScheduleActivity, "Meal deleted", Toast.LENGTH_LONG).show();
                                mealScheduleActivity.viewPagerSchedule.setAdapter(mealScheduleActivity.adapter);

                                int prevPos = prefs.getInt("LastPageList", 0);;
                                mealScheduleActivity.viewPagerSchedule.setCurrentItem(prevPos, false);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }


                    }
                };

                TextView myMsg = new TextView(mealScheduleActivity);
                myMsg.setText("\nSure you want to delete \n\n" + currentMeal.getMealScheduleRowName() + "?" );
                myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                myMsg.setTextColor(Color.BLACK);
                myMsg.setTextSize(15);

                AlertDialog.Builder builder = new AlertDialog.Builder(mealScheduleActivity);
                builder.setView(myMsg).setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();



                return true;
            }

        });*/



        //holder.textViewPriority.setText(String.valueOf(currentProduct.getId()));

   /*     holder.cardViewSchedule.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view)
            {

                mealScheduleActivity.cardViewDelete.setVisibility(View.VISIBLE);
                prefs.edit().putBoolean("DeleteMealScheduleModeOn", true).commit();
                holder.cardViewSchedule.setBackgroundColor(context.getResources().getColor(R.color.red));
                return true;
            }
        });*/

    }

    @Override
    public int getItemCount()
    {
        return meals.size();
    }

    public List<Meal> getMeals()
    {
        return this.meals;
    }

    public void setMealProducts(List<MealRow> mealProducts)
    {
        this.mealRows = mealProducts;

        notifyDataSetChanged();
    }

    public void setMeals(List<Meal> meals)
    {
        this.meals = meals;

        notifyDataSetChanged();
    }

    public Meal getMealScheduleAt(int position)
    {
        return meals.get(position);
    }


    class ShoppingListAddRecipeHolder extends  RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewDescription;
        private CardView cardViewSchedule;

        private CardView arrowInc;
        private CardView arrowDecr;

        private TextView TextViewQnt;



        public ShoppingListAddRecipeHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_title_meal);
            TextViewQnt = itemView.findViewById(R.id.text_view_quantity_meal);
            cardViewSchedule = itemView.findViewById(R.id.cv_meal_item_schedule);

            arrowDecr = itemView.findViewById(R.id.cv_qnt_meal_decrs);
            arrowInc = itemView.findViewById(R.id.cv_qnt_meal_incrs);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(meals.get(position));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {

                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();

                    if(listenerLong != null && position != RecyclerView.NO_POSITION)
                        listenerLong.onItemLongClick(meals.get(position));
                    return true;
                }
            });
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(Meal meal);
    }

    public interface OnItemLongClickListener
    {
        void onItemLongClick(Meal meal);
    }



    public void setOnMealListener(ShoppingListAddRecipeAdapter.OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public void setOnMealListenerLong(ShoppingListAddRecipeAdapter.OnItemLongClickListener listenerLong)
    {
        this.listenerLong = listenerLong;
    }

}
