package com.example.shoppinglistapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MealScheduleAdapter extends RecyclerView.Adapter<MealScheduleAdapter.MealScheduleHolder>
{
    Context context;

    public MealScheduleActivity mealScheduleActivity;
    public ShoppingListSchedulerWizardActivity shoppingListSchedulerWizard;
    private MealSchedule mealSchedule;
    private List<MealScheduleRow> mealScheduleRows = new ArrayList<>();
    private List<MealScheduleRow> mealScheduleListRows = new ArrayList<>();

    private List<MealSchedule> mealSchedules = new ArrayList<>();
    private MealScheduleAdapter.OnItemClickListener listener;

    private MealScheduleRowViewModel mealScheduleRowViewModel;
    private MealSchedule iMealSchedule;

    public MealScheduleListAdapter listAdapter;
    SharedPreferences prefs;

    public MealScheduleAdapter adapter;

    public boolean isSchedulerList = false;
    View itemView;
    private int selDays = 0;
    public List<MealRow> mealRows;


    @NonNull
    @Override
    public MealScheduleAdapter.MealScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isSchedulerList)
        {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.slide_item_schedule_shopping_list, parent,false);
        }
        else
        {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.slide_item_schedule_meal, parent,false);
        }


        return new MealScheduleAdapter.MealScheduleHolder((itemView));


    }

    @Override
    public void onBindViewHolder(@NonNull final MealScheduleAdapter.MealScheduleHolder holder, int position) {



        Log.d("MealScheduleAdapter", "OnBindViewHolder was called!");

        final MealSchedule currentMealSchedule = mealSchedules.get(position);

        Date date = currentMealSchedule.getScheduleDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);

        String strDay = new SimpleDateFormat("EE", Locale.US).format(date);


        holder.textViewHeader.setText(strDay + " ("+strDate+")");

        listAdapter = new MealScheduleListAdapter();
        listAdapter.prefs = this.prefs;

        if (isSchedulerList)
        {
            listAdapter.isSchedulerList = true;
            listAdapter.shoppingListSchedulerWizard = this.shoppingListSchedulerWizard;


            if (currentMealSchedule.getIsSelected() == 1)
            {
                holder.cardViewChooseDay.setCardBackgroundColor((ContextCompat.getColor(shoppingListSchedulerWizard,R.color.green)));
            }
            else
            {
                holder.cardViewChooseDay.setCardBackgroundColor((ContextCompat.getColor(shoppingListSchedulerWizard,R.color.white)));
            }


            holder.cardViewChooseDay.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Log.e("Clicked", "1");


                    if (currentMealSchedule.getIsSelected() == 0)
                    {
                        currentMealSchedule.setIsSelected(1);
                        iMealSchedule = currentMealSchedule;
                        Log.e("Selected", "1");
                        //Toast.makeText(ShoppingListSchedulerWizard.this, "Bravo!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        currentMealSchedule.setIsSelected(0);
                        iMealSchedule = currentMealSchedule;
                        Log.e("Selected", "0");
                    }
                    shoppingListSchedulerWizard.headerHandler.post(updateMealSelected);
                   // shoppingListSchedulerWizard.mealScheduleViewModel.update(mealSchedule);
                    //notifyDataSetChanged();

                }
            });


        }
        else
        {
            listAdapter.mealScheduleActivity = this.mealScheduleActivity;
        }



        //mealScheduleListRows.clear();
        List<MealScheduleRow> iMealScheduleListRows = new ArrayList<>();
        listAdapter.notifyDataSetChanged();


        for(MealScheduleRow mealScheduleRow : mealScheduleRows)
        {
            if (mealScheduleRow.getMealSchedule_id() == currentMealSchedule.getMealSchedule_id())
            {
                iMealScheduleListRows.add(mealScheduleRow);
            }
        }

        mealScheduleListRows = iMealScheduleListRows;

        listAdapter.setScheduleMeals(mealScheduleListRows);
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new CustomLinearLayoutManager(context,CustomLinearLayoutManager.VERTICAL, false));

        holder.recyclerView.swapAdapter(listAdapter, true);

        listAdapter.setOnMealScheduleListListener(new MealScheduleListAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(MealScheduleRow mealScheduleRow)
            {

                if (prefs.getBoolean("DeleteMealScheduleModeOn", false))
                {
                    //holder.itemView.setBackgroundColor(getColor(R.color.white));

                }
                else
                {
                    if(isSchedulerList)
                    {
                        Intent intent = new Intent(shoppingListSchedulerWizard, MealAddEditActivity.class);
                        intent.putExtra(MealAddEditActivity.EXTRA_MEAL_SCHEDULE_MEAL_VIEW,1);

                        intent.putExtra(MealAddEditActivity.EXTRA_MEAL_NAME,mealScheduleRow.getMealScheduleRowName());
                        intent.putExtra(MealAddEditActivity.EXTRA_MEAL_DESC,mealScheduleRow.getMealScheduleRowDesc());
                        intent.putExtra(MealAddEditActivity.EXTRA_MEAL_ID,mealScheduleRow.getMealId());

                        shoppingListSchedulerWizard.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(mealScheduleActivity, MealAddEditActivity.class);
                        intent.putExtra(MealAddEditActivity.EXTRA_MEAL_SCHEDULE_MEAL_VIEW,1);

                        intent.putExtra(MealAddEditActivity.EXTRA_MEAL_NAME,mealScheduleRow.getMealScheduleRowName());
                        intent.putExtra(MealAddEditActivity.EXTRA_MEAL_DESC,mealScheduleRow.getMealScheduleRowDesc());
                        intent.putExtra(MealAddEditActivity.EXTRA_MEAL_ID,mealScheduleRow.getMealId());

                        mealScheduleActivity.startActivity(intent);
                    }

                }

            }
        });


    }

    private Runnable updateMealSelected = new Runnable() {
        @Override
        public void run() {


            shoppingListSchedulerWizard.mealScheduleViewModel.update(iMealSchedule);
            shoppingListSchedulerWizard.viewPagerSchedule.setAdapter(shoppingListSchedulerWizard.adapter);

            int prevPos = prefs.getInt("LastPageList", 0);;
            shoppingListSchedulerWizard.viewPagerSchedule.setCurrentItem(prevPos, false);
            notifyDataSetChanged();



        }
    };

    @Override
    public int getItemCount()
    {
        return mealSchedules.size();
    }

    public List<MealSchedule> getMealsSchedule()
    {
        return this.mealSchedules;
    }

    public void setMealSchedules(List<MealSchedule> mealSchedules)
    {
        this.mealSchedules = mealSchedules;
        notifyDataSetChanged();
    }

    public List<MealScheduleRow> getMealScheduleRows()
    {
        return mealScheduleRows;
    }

    public void setMealSchedule(MealSchedule mealSchedule)
    {
        this.mealSchedule = mealSchedule;
        notifyDataSetChanged();
    }

    public void setMealRowSchedules(List<MealScheduleRow> mealRowSchedules)
    {
        this.mealScheduleRows = mealRowSchedules;

        notifyDataSetChanged();
    }


    public MealSchedule getMealScheduleAt(int position)
    {
        return mealSchedules.get(position);
    }




    class MealScheduleHolder extends  RecyclerView.ViewHolder
    {
        private TextView textViewHeader;
        private TextView textViewDayOfWeek;
        private RecyclerView recyclerView;
        private CardView cardViewSchedule;
        private CardView cardViewChooseDay;




        public MealScheduleHolder(@NonNull View itemView)
        {
            super(itemView);

            textViewHeader = itemView.findViewById(R.id.text_view_schedule_header);
            recyclerView = itemView.findViewById(R.id.recycler_view_schedule_meal);
            cardViewSchedule = itemView.findViewById(R.id.cv_meal_item_schedule);

            if (isSchedulerList)
                cardViewChooseDay= itemView.findViewById(R.id.cv_choose_day_to_list);



            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(mealSchedules.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(MealSchedule mealSchedule);
    }

    public void setOnMealScheduleListener(MealScheduleAdapter.OnItemClickListener listener)
    {
        this.listener = listener;
    }

}
