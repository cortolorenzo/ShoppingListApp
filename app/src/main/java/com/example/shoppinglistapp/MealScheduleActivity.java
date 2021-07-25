package com.example.shoppinglistapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MealScheduleActivity extends AppCompatActivity
{

    public ViewPager2 viewPagerSchedule;
    public MealScheduleAdapter adapter;
    private MealScheduleViewModel mealScheduleViewModel;
    public MealScheduleRowViewModel mealScheduleRowViewModel;

    public Handler headerHandler = new Handler();
    private MealSchedule currMealSchedule;

    public static final int ADD_SCHEDULE_MEAL_REQUEST = 20;
    public static final int EDIT_SCHEDULE_MEAL_REQUEST = 21;
    public static final String EXTRA_MEAL_SCHEDULE_ID = "com.example.shoppinglistapp.EXTRA_MEAL_SCHEDULE_ID";
    int currPosition;



    SharedPreferences prefs = null;

    long firstDay;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_schedule);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        prefs = getSharedPreferences("com.example.shoppinglistapp", MODE_PRIVATE);
        long firstDay = prefs.getLong("FirstDay", new Date().getTime());

        FloatingActionButton buttonAddMeal = findViewById(R.id.button_add_schedule_meal);
        buttonAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealScheduleActivity.this, MealListActivity.class);
                intent.putExtra(MealListActivity.EXTRA_MEAL_SCHEDULE_ID,currMealSchedule.getId());

                prefs.edit().putInt("LastPage", currPosition).commit();

                startActivityForResult(intent,ADD_SCHEDULE_MEAL_REQUEST);

            }


        });



        viewPagerSchedule = findViewById(R.id.viewPagerMealSchedule);
        viewPagerSchedule.setVisibility(View.GONE);

        adapter = new MealScheduleAdapter();
        adapter.mealScheduleActivity = MealScheduleActivity.this;
        adapter.prefs = getSharedPreferences("com.example.shoppinglistapp", MODE_PRIVATE);
        viewPagerSchedule.setAdapter((adapter));

        viewPagerSchedule.setClipToPadding(false);
        viewPagerSchedule.setClipChildren(false);
        viewPagerSchedule.setOffscreenPageLimit(3);
        viewPagerSchedule.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer()
        {
            @Override
            public void transformPage(@NonNull View page, float position)
            {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }

        });



        viewPagerSchedule.setPageTransformer(compositePageTransformer);


        mealScheduleRowViewModel = new ViewModelProvider(this
        , ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
        .get(MealScheduleRowViewModel.class);


        mealScheduleRowViewModel.GetMealsForSchedule().observe(this, new Observer<List<MealScheduleRow>>()
        {
            @Override
            public void onChanged(List<MealScheduleRow> mealScheduleRows)
            {
                //update viewPager

                adapter.setMealRowSchedules(mealScheduleRows);
                //Toast.makeText(ProductListActivity.this, "Use + to add a new product",Toast.LENGTH_LONG).show();
            }
        });


        mealScheduleViewModel = new ViewModelProvider(this
                , ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MealScheduleViewModel.class);

        SetDatesParam setDatesParam = new SetDatesParam();

        Log.d("Daty: ", String.valueOf(setDatesParam.startDate) + " " + String.valueOf(setDatesParam.endDate));

        mealScheduleViewModel.GetAllMealSchedules(setDatesParam.startDate, setDatesParam.endDate).observe(this, new Observer<List<MealSchedule>>()
        {
            @Override
            public void onChanged(List<MealSchedule> mealSchedules)
            {
                //update viewPager

                adapter.setMealSchedules(mealSchedules);
                //Toast.makeText(ProductListActivity.this, "Use + to add a new product",Toast.LENGTH_LONG).show();
            }
        });

        headerHandler.removeCallbacks(headerRunnable);
        headerHandler.postDelayed(headerRunnable, 400);




        viewPagerSchedule.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                viewPagerSchedule.setCurrentItem(position);

                currMealSchedule = adapter.getMealScheduleAt(position);
                currPosition = position;
                prefs.edit().putInt("LastPageList", currPosition).commit();
                Log.e("Selected_Page", String.valueOf(position));




            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }

        });


    }


 /* public void viewDelete (View v)
    {
        SetScheduleMealDeleteModeOff();
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_SCHEDULE_MEAL_REQUEST && resultCode == RESULT_OK)
        {
            //adapter.notifyDataSetChanged();
            Toast.makeText(this,"Meal added to schedule", Toast.LENGTH_SHORT).show();
        }

        viewPagerSchedule.setAdapter(adapter);
        headerHandler.removeCallbacks(repositionRunnable);
        headerHandler.post(repositionRunnable);

    }

    private Runnable headerRunnable = new Runnable() {
        @Override
        public void run() {


            int positionToDay = adapter.getItemCount() - 101;
            viewPagerSchedule.setCurrentItem(positionToDay, false);



            viewPagerSchedule.setVisibility(View.VISIBLE);
            Log.e("Selected_Page", String.valueOf(positionToDay));

        }
    };

    public Runnable repositionRunnable = new Runnable() {
        @Override
        public void run() {

            int prevPos = prefs.getInt("LastPage", 0);;
            viewPagerSchedule.setCurrentItem(prevPos, false);
            Log.e("PrevPos", String.valueOf(prevPos));


        }
    };



    public class SetDatesParam
    {
        private Date startDate;
        private Date endDate;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currDate = new Date();

        SetDatesParam()
        {

            Calendar c = Calendar.getInstance();
            long milliDiff = c.get(Calendar.ZONE_OFFSET);
            // Got local offset, now loop through available timezone id(s).
            String [] ids = TimeZone.getAvailableIDs();
            String name = null;

            for(String id : ids)
            {
                TimeZone tz = TimeZone.getTimeZone(id);
                if (tz.getRawOffset() == milliDiff)
                {
                    name = id;
                    break;
                }
            }

            TimeZone tz = TimeZone.getTimeZone(name);
            c.setTimeZone(tz);


            c.setTime(currDate);
            c.add(Calendar.DATE, -100);

            this.startDate = c.getTime();

            c.setTime(currDate);
            c.add(Calendar.DATE, 100);

            this.endDate = c.getTime();

        }


    }
}