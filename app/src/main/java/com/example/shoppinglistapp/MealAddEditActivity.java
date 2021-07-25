package com.example.shoppinglistapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MealAddEditActivity extends AppCompatActivity {

    public static final String EXTRA_MEAL_NAME =
            "com.example.shoppinglistapp.EXTRA_MEAL_NAME";
    public static final String EXTRA_MEAL_DESC =
            "com.example.shoppinglistapp.EXTRA_MEAL_DESC";

    public static final String EXTRA_INSERTED_MEAL_ID =
            "com.example.shoppinglistapp.EXTRA_INSERTED_MEAL_ID";
    public static final String EXTRA_MEAL_ID =
            "com.example.shoppinglistapp.EXTRA_MEAL_ID";

    public static final String EXTRA_IS_UPDATED =
            "com.example.shoppinglistapp.EXTRA_IS_UPDATED";

    public static final int ADD_MEAL_PRODUCT_REQUEST = 1;

    public static final String EXTRA_MEAL_SCHEDULE_MEAL_VIEW =
            "com.example.shoppinglistapp.EXTRA_MEAL_SCHEDULE_MEAL_VIEW";

    private EditText editTextName;
    private EditText editTextDesc;

    private MealViewModel mealViewModel;
    private MealAdapter mealAdapter;

    private MealRowViewModel mealRowViewModel;
    public MealRowAdapter mealRowAdapter;

    int idMeal;
    int idMealInsert;
    int czyEditMode = 0;

    RecyclerView recyclerView;
    LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);

    String inName;
    String inDesc;

    List<MealRow> inputMealRows = new ArrayList<>();
    int bckFlag = 0;

    boolean isChanged;

    boolean isUpdated(String outName, String outDesc)
    {
        boolean sameName = false;
        boolean sameDesc = false;
        int cntRowsFlag = 0;

        Log.d("boolean in Name ", String.valueOf(inName));


        if(inName.equals(outName))
            sameName = true;


        if (inDesc.equals(outDesc))
            sameDesc = true;


        for(MealRow  mealRow :mealRowAdapter.getMealsFlagged() )
        {
            Log.d("Flagged id ", String.valueOf(mealRow.getId()));
        }

        cntRowsFlag = mealRowAdapter.getMealsFlagged().size();


        if (sameName == false || sameDesc == false || cntRowsFlag > 0 || isQntChanged() == true)
            isChanged = true;

        Log.d("boolean QntChanged ", String.valueOf(isQntChanged()));
        Log.d("boolean ischanged ", String.valueOf(isChanged));
        return isChanged;

    }



    boolean isQntChanged()
    {

        List<MealRow> mealRowsOut = mealRowAdapter.getMealProducts();
        Log.d("int SIZE  ", String.valueOf(mealRowsOut.size()));
        Log.d("int SIZE in  ", String.valueOf(inputMealRows.size()));
        for(MealRow mealRowIn : inputMealRows)
        {
            int in = mealRowIn.getId();

            Log.d("int QntIn ", String.valueOf(mealRowIn.getQuantity()));
            for(MealRow mealRowOut : mealRowsOut)
            {
                int out = mealRowOut.getId();

                Log.d("int QntOut ", String.valueOf(mealRowOut.getQuantity()));
                if (in == out)
                {
                    double d1 = mealRowIn.getQuantity();
                    double d2 = mealRowOut.getQuantity();

                    Log.d("int QntOut ", "Comparing doubles!");
      /*              Log.d("RES ", String.valueOf(res));
                    Log.d("RES ", String.valueOf((Math.abs(res - 1.0) >= 0.000001)));*/
                    if(Double.compare(d1,d2) != 0)
                    {
                        Log.d("int QntOut ", "EQUAL!");
                        return true;
                    }
                }
            }
        }

        return false;
    }


    void refreshMeals(){

        mealRowViewModel.getProductsForMeal(idMeal).observe(this, new Observer<List<MealRow>>() {
            @Override
            public void onChanged(List<MealRow> mealRows) {

                mealRowAdapter.setMealProducts(mealRows);


            }});
    }

    public void getInputMealRows()
    {
        refreshMeals();
        //inputMealRows = mealRowAdapter.getMealProducts();
        new getInputMealRowsTask().execute();
    }

    public class getInputMealRowsTask extends AsyncTask<Void, Void, Void>
    {
        List<MealRow> mealRowsAsync = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... voids)
        {

            int cnt = 0;
            while (mealRowsAsync.size() == 0 && bckFlag == 0 && cnt < 50000 )
            {

                mealRowsAsync = mealRowAdapter.getMealProducts();
                Log.d("BGW", String.valueOf(mealRowsAsync.size()) + " " + String.valueOf(cnt));
                if (mealRowsAsync.size() > 0)
                {
                    bckFlag = 1;
                }

                cnt++;
            }

            inputMealRows = mealRowsAsync;

            return null;
        }

    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Log.d("Inserted Meal back ", String.valueOf(idMealInsert));

        String name = editTextName.getText().toString();
        String desc = editTextDesc.getText().toString();

        Intent data = new Intent();

        data.putExtra(EXTRA_INSERTED_MEAL_ID, idMealInsert);
        Log.d("idMeal on back", String.valueOf(idMeal));
        data.putExtra(EXTRA_MEAL_ID, idMeal);
        data.putExtra(EXTRA_IS_UPDATED, isUpdated(name, desc));

        setResult(RESULT_CANCELED,data);
        finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_add_edit);

        editTextName = findViewById(R.id.edit_text_name_meal);
        editTextDesc = findViewById(R.id.edit_text_desc_meal);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_MEAL_ID))
        {
            setTitle("Edit Meal");
            editTextName.setText(intent.getStringExtra(EXTRA_MEAL_NAME));
            editTextDesc.setText(intent.getStringExtra(EXTRA_MEAL_DESC));
            czyEditMode = 1;


        }
        else
        {
            czyEditMode = 0;
            setTitle("Define new recipe");
        }


        mealViewModel = new ViewModelProvider(this
                , ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MealViewModel.class);

        mealViewModel.getLastMeal().observe(MealAddEditActivity.this, new Observer<Meal>() {
            @Override
            public void onChanged(Meal lastmeal) {

                mealAdapter.setMeal(lastmeal);

            }
        });


        mealAdapter = new MealAdapter();

        recyclerView = findViewById(R.id.recycler_view_product_meal);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setHasFixedSize(true);

        mealRowAdapter = new MealRowAdapter();
        recyclerView.setAdapter((mealRowAdapter));


        mealRowViewModel = new ViewModelProvider(this
                , ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MealRowViewModel.class);


        idMeal = getIntent().getIntExtra(EXTRA_MEAL_ID, -1);



            mealRowViewModel.getProductsForMeal(idMeal).observe(this, new Observer<List<MealRow>>() {
                @Override
                public void onChanged(List<MealRow> mealRows) {

                    mealRowAdapter.setMealProducts(mealRows);



                }});


        mealRowViewModel.getFlaggedMeals(idMeal).observe(this, new Observer<List<MealRow>>() {
            @Override
            public void onChanged(List<MealRow> mealRows) {

                mealRowAdapter.setMealFlagged(mealRows);


            }});

        if (czyEditMode == 1)
            getInputMealRows();
        inName = editTextName.getText().toString();
        inDesc = editTextDesc.getText().toString();

        FloatingActionButton buttonAddProduct = findViewById(R.id.button_add_product);

//Entering the Activity form ScheduleActivity
        Intent intentMealSchedule = getIntent();
        if(intent.hasExtra(EXTRA_MEAL_SCHEDULE_MEAL_VIEW))
        {
            buttonAddProduct.setVisibility(View.GONE);
            getSupportActionBar().hide();

            editTextName.setFocusable(false);
            editTextName.setFocusableInTouchMode(false);
            editTextName.setInputType(InputType.TYPE_NULL);

            editTextDesc.setFocusable(false);
            editTextDesc.setFocusableInTouchMode(false);
            editTextDesc.setSingleLine(false);
            editTextDesc.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

            mealRowAdapter.scheduleAcivityRequest = true;

        }


        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentParent = getIntent();
                int id = getIntent().getIntExtra(EXTRA_MEAL_ID, -1);

                String name = editTextName.getText().toString();
                String desc = editTextDesc.getText().toString();


                ArrayList<MealRow> list = (ArrayList<MealRow>) mealRowAdapter.getMealProducts();

                if (czyEditMode != 1 && idMealInsert == 0)
                {
                    if (name.trim().isEmpty()) {
                        Toast.makeText(MealAddEditActivity.this, "Please insert meal name first", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Meal meal = new Meal(name, desc);
                    int lastID = (int) mealViewModel.insert(meal);


                    idMealInsert = lastID;

                    idMeal = idMealInsert;
                    mealRowViewModel.getProductsForMeal(idMealInsert).observe(MealAddEditActivity.this, new Observer<List<MealRow>>() {
                        @Override
                        public void onChanged(List<MealRow> mealRows) {

                            mealRowAdapter.setMealProducts(mealRows);

                        }});

                    mealRowAdapter.notifyDataSetChanged();

                    Log.d("con idMEAL: ",String.valueOf(idMeal));
                    Log.d("con idMEAL inserted: ",String.valueOf(lastID));

                    List<MealRow> listMealRow = new ArrayList<>();
                    listMealRow = mealRowAdapter.getMealProducts();




                    Intent intent = new Intent(MealAddEditActivity.this, MealProductAddActivity.class);
                    intent.putExtra(MealProductAddActivity.EXTRA_MEAL_ID, idMealInsert );
                    intent.putExtra(MealProductAddActivity.CZY_EDIT, czyEditMode );
                    intent.putExtra("LIST",(Serializable) listMealRow);
                    intent.putExtra("list",list);
                    startActivityForResult(intent,ADD_MEAL_PRODUCT_REQUEST);
                    return;

                }

                updateDBList();

                List<MealRow> listMealRow = new ArrayList<>();
                listMealRow = mealRowAdapter.getMealProducts();


                Intent intent = new Intent(MealAddEditActivity.this, MealProductAddActivity.class);
                intent.putExtra(MealProductAddActivity.EXTRA_MEAL_ID, idMeal );
                intent.putExtra(MealProductAddActivity.CZY_EDIT, czyEditMode );
                intent.putExtra("LIST",(Serializable) listMealRow);
                intent.putExtra("list",list);
                startActivityForResult(intent,ADD_MEAL_PRODUCT_REQUEST);

            }


        });


        if(!intent.hasExtra(EXTRA_MEAL_SCHEDULE_MEAL_VIEW))
        {
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

                                    //mealRowViewModel.delete(mealRowAdapter.getMealProductAt(viewHolder.getAdapterPosition()));

                                    MealRow mealRow = mealRowAdapter.getMealProductAt(viewHolder.getAdapterPosition());
                                    mealRow.setDeleteFlag(1);

                                    mealRowViewModel.updateDeleted(mealRow);


                                    Toast.makeText(MealAddEditActivity.this, "Product deleted", Toast.LENGTH_LONG).show();
                                    updateDBList();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    mealRowAdapter.notifyDataSetChanged();
                                    break;
                            }


                        }
                    };

                    TextView myMsg = new TextView(MealAddEditActivity.this);
                    myMsg.setText("\nSure you want to delete \n\n" + mealRowAdapter.getMealProductAt(viewHolder.getAdapterPosition()).getProductName() + "?" );
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                    myMsg.setTextColor(Color.BLACK);
                    myMsg.setTextSize(15);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MealAddEditActivity.this);
                    builder.setView(myMsg).setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();


                }
            }).attachToRecyclerView(recyclerView);

        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == ADD_MEAL_PRODUCT_REQUEST && resultCode == RESULT_OK)
        {
            //int idMeal = data.getIntExtra(MealProductAddActivity.EXTRA_MEAL_ID, -1);

            Log.d("idMEAL: ",String.valueOf(idMeal));
            Log.d("idMEAL inserted: ",String.valueOf(idMealInsert));


            int idProduct = data.getIntExtra(MealProductAddActivity.EXTRA_PRODUCT_ID, -1);


            //EDIT MODE
            if(czyEditMode == 1)
            {
                Log.d("EDIT MODE ADD: ", "test");
                MealRow mealRow = new MealRow(idMeal,idProduct, 1);
                mealRowViewModel.insert(mealRow);

                mealRow.setInsertFlag(1);
                mealRowViewModel.updateInserted(mealRow);


            }

            //ADD MODE
            if(czyEditMode == 0)
            {
               /* idMeal = idMealInsert;
                mealRowViewModel.getProductsForMeal(idMealInsert).observe(this, new Observer<List<MealRow>>() {
                    @Override
                    public void onChanged(List<MealRow> mealRows) {

                        mealRowAdapter.setMealProducts(mealRows);

                    }});

                MealRow mealRow = new MealRow(idMealInsert,idProduct, 0);
                long insertedRow = mealRowViewModel.insert(mealRow);

                recyclerView.swapAdapter(mealRowAdapter,false);
             *//*   recyclerView.setAdapter(null);
                recyclerView.setLayoutManager(null);
                recyclerView.setAdapter(mealRowAdapter);*//*
                recyclerView.setLayoutManager(myLayoutManager);
                recyclerView.getRecycledViewPool().clear();
                recyclerView.getLayoutManager().removeAllViews();
                mealRowAdapter.notifyDataSetChanged();
*/




            }



            Toast.makeText(this,"Product added", Toast.LENGTH_SHORT).show();


        }

        else
        {
            if(requestCode == ADD_MEAL_PRODUCT_REQUEST){
                Toast.makeText(this,"Product not added", Toast.LENGTH_SHORT).show();
            }


        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_product_menu,menu);

        return true;
    }

    private void saveMeal()
    {
        String name = editTextName.getText().toString();
        String desc = editTextDesc.getText().toString();

        if (name.trim().isEmpty())
        {
            Toast.makeText(this,"Please insert Name", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_MEAL_NAME, name);
        data.putExtra(EXTRA_MEAL_DESC, desc);
        data.putExtra(EXTRA_INSERTED_MEAL_ID, idMealInsert);

        int id = getIntent().getIntExtra(EXTRA_MEAL_ID,-1);
        if (id != -1)
        {
            data.putExtra(EXTRA_MEAL_ID, id);

        }

        updateDBList();
        data.putExtra(EXTRA_IS_UPDATED, isUpdated(name, desc));
        setResult(RESULT_OK,data);


        finish();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch  (item.getItemId())
        {
            case R.id.save_product:
                saveMeal();
                break;

            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return  true;


    }

    public void updateDBList()
    {
        for(MealRow mealRow : mealRowAdapter.getMealProducts())
        {
            mealRowViewModel.update(mealRow);
        }
        mealRowAdapter.notifyDataSetChanged();

    }
}
