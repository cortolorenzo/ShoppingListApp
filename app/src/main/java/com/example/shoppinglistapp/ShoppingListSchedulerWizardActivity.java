package com.example.shoppinglistapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class ShoppingListSchedulerWizardActivity extends AppCompatActivity {

    public ViewPager2 viewPagerSchedule;
    public MealScheduleAdapter adapter;
    public MealScheduleViewModel mealScheduleViewModel;
    public MealScheduleRowViewModel mealScheduleRowViewModel;
    Context context;

    public Handler headerHandler = new Handler();
    private MealSchedule currMealSchedule;

    private TextView tvSelDays;

    public static final int ADD_SCHEDULE_MEAL_REQUEST = 20;
    public static final int EDIT_SCHEDULE_MEAL_REQUEST = 21;
    public static final String EXTRA_MEAL_SCHEDULE_ID = "com.example.shoppinglistapp.EXTRA_MEAL_SCHEDULE_ID";
    int currPosition;

    AlertDialog alertDialog;
    int insertedListId;
    private ShoppingListViewModel shoppingListViewModel;
    private MealRowViewModel mealRowViewModel;
    private List<MealScheduleRow> iSelectedMealScheduleRows;


    SharedPreferences prefs = null;

    long firstDay;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_scheduler_wizard);
        context = this;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        prefs = getSharedPreferences("com.example.shoppinglistapp", MODE_PRIVATE);
        long firstDay = prefs.getLong("FirstDay", new Date().getTime());

   /*     FloatingActionButton buttonAddMeal = findViewById(R.id.button_add_schedule_meal);
        buttonAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingListSchedulerWizard.this, MealListActivity.class);
                intent.putExtra(MealListActivity.EXTRA_MEAL_SCHEDULE_ID,currMealSchedule.getId());

                prefs.edit().putInt("LastPage", currPosition).commit();

                startActivityForResult(intent,ADD_SCHEDULE_MEAL_REQUEST);

            }


        });*/


        tvSelDays = findViewById(R.id.tv_sel_days);
        viewPagerSchedule = findViewById(R.id.viewPagerMealSchedule);
        viewPagerSchedule.setVisibility(View.GONE);

        adapter = new MealScheduleAdapter();
        adapter.shoppingListSchedulerWizard = ShoppingListSchedulerWizardActivity.this;
        adapter.isSchedulerList = true;
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

        shoppingListViewModel = new ViewModelProvider(this
                ,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(ShoppingListViewModel.class);

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
        mealRowViewModel = new ViewModelProvider(this
                , ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MealRowViewModel.class);

        mealRowViewModel.getAllMealRows().observe(this, new Observer<List<MealRow>>()
        {
            @Override
            public void onChanged(List<MealRow> mealRows)
            {
                adapter.mealRows = mealRows;
            }});

        mealScheduleViewModel = new ViewModelProvider(this
                , ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MealScheduleViewModel.class);

        ShoppingListSchedulerWizardActivity.SetDatesParam setDatesParam = new ShoppingListSchedulerWizardActivity.SetDatesParam();

        Log.d("Daty: ", String.valueOf(setDatesParam.startDate) + " " + String.valueOf(setDatesParam.endDate));


        mealScheduleViewModel.GetAllMealSchedules(setDatesParam.startDate, setDatesParam.endDate).observe(this, new Observer<List<MealSchedule>>()
        {
            @Override
            public void onChanged(List<MealSchedule> mealSchedules)
            {
                //update viewPager

                adapter.setMealSchedules(mealSchedules);
                updateSelDaysCnt(false);
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

    public void updateSelDaysCnt(boolean isClear)
    {

        int cnt = 0;
        if (isClear)
        {
            for(MealSchedule mealSchedule : adapter.getMealsSchedule())
            {
                if (mealSchedule.getIsSelected() == 1)
                {
                    mealSchedule.setIsSelected(0);
                    mealScheduleViewModel.update(mealSchedule);
                    cnt = 0;
                }

            }
        }
        else
        {
            for(MealSchedule mealSchedule : adapter.getMealsSchedule())
            {
                if (mealSchedule.getIsSelected() == 1)
                    cnt++;
            }
        }

        String descr = "Selected days " + String.valueOf(cnt);

        tvSelDays.setText(descr);

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
            updateSelDaysCnt(true);
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


    public boolean SelectedMeals()
    {

        // Selected days collction
        List<MealSchedule> selectedDays = new ArrayList<>();

        for(MealSchedule mealSchedule : adapter.getMealsSchedule())
        {
            if (mealSchedule.getIsSelected() == 1)
            {
                selectedDays.add(mealSchedule);
            }
        }


/*
        headerHandler.removeCallbacks(SaveRecipe);
        headerHandler.postDelayed(SaveRecipe, 100);*/

        //Getting mealrows form selected days
        List<MealScheduleRow> selectedMealScheduleRows = new ArrayList<>();

        for(MealSchedule mealSchedule : selectedDays)
        {
            for(MealScheduleRow mealScheduleRow : adapter.getMealScheduleRows())
            {
                if (mealScheduleRow.getMealSchedule_id() == mealSchedule.getMealSchedule_id())
                {
                    selectedMealScheduleRows.add(mealScheduleRow);
                }
            }
        }

        iSelectedMealScheduleRows = selectedMealScheduleRows;

        if (iSelectedMealScheduleRows.size() > 0)
            return true;
        else
            return false;


    }
    public void makeList(View v)
    {
        if (!SelectedMeals())
        {
            Toast.makeText(ShoppingListSchedulerWizardActivity.this, "No meals to add", Toast.LENGTH_SHORT).show();
            return;
        }


        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_save_list, null);
        final EditText input = (EditText) view.findViewById(R.id.etComments);

        AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingListSchedulerWizardActivity.this);
        builder.setTitle("Save list")
                .setMessage("Enter your list name")
                .setView(view);

        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        alertDialog = builder.create();


        alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {

                Button button   = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view)
                    {
                        String listName;
                        listName = input.getText().toString();

                        if (listName.length() > 0)
                        {

                            Calendar c = Calendar.getInstance();
                            Date date = new Date();
                            c.setTime(date);
                            date = c.getTime();

                            ShoppingList shoppingList = new ShoppingList(listName, "SL", date);

                            insertedListId = (int) shoppingListViewModel.insert(shoppingList);
                            SaveListHandler(insertedListId);

                            Toast.makeText(ShoppingListSchedulerWizardActivity.this, "Hurray", Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();

                            Intent intentPreviewList = new Intent(ShoppingListSchedulerWizardActivity.this, ShoppingListListActivity.class);
                            startActivity(intentPreviewList);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(ShoppingListSchedulerWizardActivity.this, "Enter list Name", Toast.LENGTH_SHORT).show();
                        }



                    }
                });

            }
        });

        alertDialog.show();

        //updateDBList();


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void SaveListHandler(int ListID)
    {



        //Getting actual Meal row products

        List<ProductToList> productsToListUnGr = new ArrayList<>();

        for(MealScheduleRow mealScheduleRow : iSelectedMealScheduleRows)
        {
            for(MealRow mealRow : adapter.mealRows )
            {
                if (mealRow.getMealId() == mealScheduleRow.getMealId())
                {
                    double qnt = mealRow.getQuantity() * mealScheduleRow.getMealQnt();
                    ProductToList productToList = new ProductToList(mealRow.getProductId(), qnt);

                    productsToListUnGr.add(productToList);
                }
            }
        }

        //Grouping products by ProductID

        List<ProductToList> productsToListGrp =

                productsToListUnGr.stream()
                .collect(Collectors.groupingBy(ProductToList::getProductID))
                .entrySet().stream()
                .map(e -> e.getValue().stream()
                        .reduce((f1,f2) -> new ProductToList(f1.productID, f1.qnt + f2.qnt)))
                        .map(f -> f.get())
                        .collect(Collectors.toList());

        //Inserting the actual meal rows to shopping list
        for(ProductToList productToList : productsToListGrp)
        {
            ShoppingListRow shoppingListRow = new ShoppingListRow(insertedListId,productToList.getProductID(),productToList.getQnt(),0,0);
            shoppingListViewModel.insert(shoppingListRow);
        }






    }

    public static class ProductToList
    {
        public int productID;
        public double qnt;

        public int getProductID() {
            return productID;
        }

        public void setProductID(int productID) {
            this.productID = productID;
        }

        public double getQnt() {
            return qnt;
        }

        public void setQnt(double qnt) {
            this.qnt = qnt;
        }

        public ProductToList(int productID, double qnt)
        {
            this.productID = productID;
            this.qnt    = qnt;
        }


    }

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