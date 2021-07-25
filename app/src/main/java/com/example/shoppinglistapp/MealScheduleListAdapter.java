package com.example.shoppinglistapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MealScheduleListAdapter extends RecyclerView.Adapter<MealScheduleListAdapter.MealScheduleListHolder>
{

    Context context;
    private MealScheduleRow mealScheduleRow;
    private List<MealScheduleRow> scheduleMeals = new ArrayList<>();

    private MealScheduleListAdapter.OnItemClickListener listener;
    private MealScheduleListAdapter.OnItemLongClickListener listenerLong;
    public MealScheduleActivity mealScheduleActivity;
    public ShoppingListSchedulerWizardActivity shoppingListSchedulerWizard;
    public boolean isSchedulerList = false;


    SharedPreferences prefs;
    List<CardView> cardViews = new ArrayList<>();

    public MealScheduleAdapter adapter;
    private int lastPosition = -1;
    CardView selectedCard;

    @NonNull
    @Override
    public MealScheduleListAdapter.MealScheduleListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_item_schedule, parent,false);



        return new MealScheduleListAdapter.MealScheduleListHolder((itemView));


    }


    @Override
    public void onBindViewHolder(@NonNull final MealScheduleListAdapter.MealScheduleListHolder holder, final int position)
    {
        final MealScheduleRow currentMeal = scheduleMeals.get(position);
        holder.textViewTitle.setText(currentMeal.getMealScheduleRowName());

        holder.TextViewQnt.setText(String.valueOf(currentMeal.getMealQnt()));

        Log.e("MealScheduleListAdapter", "OnBindViewHolder was called!");


        //cardViews.add(holder.cardViewSchedule);


        if(!isSchedulerList)
        {
            holder.arrowInc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    int prevQnt = currentMeal.getMealQnt();
                    int newQnt = prevQnt + 1;

                    currentMeal.setMealQnt(newQnt);

                    mealScheduleActivity.mealScheduleRowViewModel.updateMealScheduleRow(currentMeal);
                    mealScheduleActivity.viewPagerSchedule.setAdapter(mealScheduleActivity.adapter);

                    int prevPos = prefs.getInt("LastPageList", 0);;
                    mealScheduleActivity.viewPagerSchedule.setCurrentItem(prevPos, false);

                }
            });


            holder.arrowDecr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {

                    int newQnt;

                    int prevQnt = currentMeal.getMealQnt();
                    if (prevQnt == 1 || prevQnt == 0)
                    {
                        newQnt = 0;
                    }
                    else
                    {
                        newQnt= prevQnt - 1;
                    }


                    currentMeal.setMealQnt(newQnt);

                    mealScheduleActivity.mealScheduleRowViewModel.updateMealScheduleRow(currentMeal);
                    mealScheduleActivity.viewPagerSchedule.setAdapter(mealScheduleActivity.adapter);

                    int prevPos = prefs.getInt("LastPageList", 0);;
                    mealScheduleActivity.viewPagerSchedule.setCurrentItem(prevPos, false);

                }
            });


            holder.cardViewSchedule.setOnLongClickListener(new View.OnLongClickListener()
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

            });
        }
        else
        {
            holder.arrowInc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    int prevQnt = currentMeal.getMealQnt();
                    int newQnt = prevQnt + 1;

                    currentMeal.setMealQnt(newQnt);

                    shoppingListSchedulerWizard.mealScheduleRowViewModel.updateMealScheduleRow(currentMeal);
                    shoppingListSchedulerWizard.viewPagerSchedule.setAdapter(shoppingListSchedulerWizard.adapter);

                    int prevPos = prefs.getInt("LastPageList", 0);;
                    shoppingListSchedulerWizard.viewPagerSchedule.setCurrentItem(prevPos, false);

                }
            });


            holder.arrowDecr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {

                    int newQnt;

                    int prevQnt = currentMeal.getMealQnt();
                    if (prevQnt == 1 || prevQnt == 0)
                    {
                        newQnt = 0;
                    }
                    else
                    {
                        newQnt= prevQnt - 1;
                    }


                    currentMeal.setMealQnt(newQnt);

                    shoppingListSchedulerWizard.mealScheduleRowViewModel.updateMealScheduleRow(currentMeal);
                    shoppingListSchedulerWizard.viewPagerSchedule.setAdapter(shoppingListSchedulerWizard.adapter);

                    int prevPos = prefs.getInt("LastPageList", 0);;
                    shoppingListSchedulerWizard.viewPagerSchedule.setCurrentItem(prevPos, false);

                }
            });


            holder.cardViewSchedule.setOnLongClickListener(new View.OnLongClickListener()
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

                                    shoppingListSchedulerWizard.mealScheduleRowViewModel.deleteMealScheduleRow(currentMeal);
                                    Toast.makeText(shoppingListSchedulerWizard, "Meal deleted", Toast.LENGTH_LONG).show();
                                    shoppingListSchedulerWizard.viewPagerSchedule.setAdapter(shoppingListSchedulerWizard.adapter);

                                    int prevPos = prefs.getInt("LastPageList", 0);;
                                    shoppingListSchedulerWizard.viewPagerSchedule.setCurrentItem(prevPos, false);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }


                        }
                    };

                    TextView myMsg = new TextView(shoppingListSchedulerWizard);
                    myMsg.setText("\nSure you want to delete \n\n" + currentMeal.getMealScheduleRowName() + "?" );
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                    myMsg.setTextColor(Color.BLACK);
                    myMsg.setTextSize(15);

                    AlertDialog.Builder builder = new AlertDialog.Builder(shoppingListSchedulerWizard);
                    builder.setView(myMsg).setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();



                    return true;
                }

            });




        }




    }

    @Override
    public int getItemCount()
    {
        return scheduleMeals.size();
    }

    public List<MealScheduleRow> getMeals()
    {
        return this.scheduleMeals;
    }

    public void setMealSchedule(MealScheduleRow mealScheduleRow)
    {
        this.mealScheduleRow = mealScheduleRow;
        notifyDataSetChanged();
    }

    public MealScheduleRow getMealSchedule()
    {
        return this.mealScheduleRow;
    }


    public void setScheduleMeals(List<MealScheduleRow> mealScheduleRows)
    {
        this.scheduleMeals = mealScheduleRows;

        notifyDataSetChanged();
    }

    public MealScheduleRow getMealScheduleAt(int position)
    {
        return scheduleMeals.get(position);
    }


    class MealScheduleListHolder extends  RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewDescription;
        private CardView cardViewSchedule;

        private CardView arrowInc;
        private CardView arrowDecr;

        private TextView TextViewQnt;




        public MealScheduleListHolder(@NonNull View itemView) {
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
                        listener.onItemClick(scheduleMeals.get(position));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {

                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();

                  /*  mealScheduleActivity.cardViewDelete.setVisibility(View.VISIBLE);
                    prefs.edit().putBoolean("DeleteMealScheduleModeOn", true).commit();
                    view.setBackgroundColor(ContextCompat.getColor(mealScheduleActivity,R.color.green));*/

                    if(listenerLong != null && position != RecyclerView.NO_POSITION)
                        listenerLong.onItemLongClick(scheduleMeals.get(position));
                    return true;
                }
            });
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(MealScheduleRow mealScheduleRow);
    }

    public interface OnItemLongClickListener
    {
        void onItemLongClick(MealScheduleRow mealScheduleRow);
    }



    public void setOnMealScheduleListListener(MealScheduleListAdapter.OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public void setOnMealScheduleListListenerLong(MealScheduleListAdapter.OnItemLongClickListener listenerLong)
    {
        this.listenerLong = listenerLong;
    }


}
