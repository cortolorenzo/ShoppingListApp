package com.example.shoppinglistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ShoppingListAddEditActivity extends AppCompatActivity
{

    // 1 for adding new list, 0 for editing existing one
    public static final String MODE_ADD_SHOPPING_lIST = "com.example.shoppinglistapp.MODE_ADD_SHOPPING_lIST";
    public static final String MODE_SHOPPING_lIST_TYPE = "com.example.shoppinglistapp.MODE_SHOPPING_lIST_TYPE";
    public static final String SHOPPING_LIST_ID = "com.example.shoppinglistapp.MODE_SHOPPING_lIST_TYPE";

    public static final int ADD_LIST_PRODUCT_REQUEST = 32;
    public static final int ADD_LIST_RECIPE_REQUEST = 33;

    private ShoppingListViewModel shoppingListViewModel;
    int insertedListId;

    // Intent Variables read
    private int viewMode = 0;
    private int listType;
    private String sListType;


    private int listId;
    Context context = this;
    public boolean isEdit;

    public ShoppingListEditAdapter adapter;
    AlertDialog alertDialog;
    boolean alertReady;
    TextView editTextSaveButton;

    List<ShoppingListRow> iShoppingListRows;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_add_edit);

        editTextSaveButton = findViewById(R.id.tv_save_button);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        RecyclerView recyclerView = findViewById(R.id.rc_shop_list_product);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);



        adapter = new ShoppingListEditAdapter();
        recyclerView.setAdapter((adapter));

        shoppingListViewModel = new ViewModelProvider(this
                ,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(ShoppingListViewModel.class);


// Caller source identification
        Intent intent = getIntent();
        viewMode = intent.getIntExtra(MODE_ADD_SHOPPING_lIST, 0);
        listType = intent.getIntExtra(MODE_SHOPPING_lIST_TYPE, 0);

        switch (listType)
        {
            case 0:
                sListType = "SL";
                break;
            case 1:
                sListType = "QL";
                break;
        }



        if (viewMode == 1)
        {
            listId = -1;
            adapter.isEdit = false;
        }
        else
        {
            listId = intent.getIntExtra(SHOPPING_LIST_ID,0);
            editTextSaveButton.setText("SAVE CHANGES");
            adapter.isEdit = true;
        }

        shoppingListViewModel.getAllShoppingListRows(listId).observe(this, new Observer<List<ShoppingListRow>>()
        {
            @Override
            public void onChanged(List<ShoppingListRow> shoppingListRows)
            {
                Log.e("query", String.valueOf(listId));

                iShoppingListRows = new ArrayList<>();
                for (ShoppingListRow shoppingListRow : shoppingListRows)
                {
                    if(shoppingListRow.getFlag() != 2)
                    {
                        iShoppingListRows.add(shoppingListRow);
                    }

                }
                adapter.setShoppingListRows(iShoppingListRows);
            }
        });

//Deleting items from the list
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

                                ShoppingListRow shoppingListRow = adapter.getShoppingListRowAt(viewHolder.getAdapterPosition());
                                if(listId != -1)
                                {
                                    shoppingListRow.setFlag(2);
                                    shoppingListViewModel.update(shoppingListRow);
                                }
                                else
                                {
                                    shoppingListViewModel.delete(shoppingListRow);
                                }


                                Toast.makeText(ShoppingListAddEditActivity.this, "Product deleted", Toast.LENGTH_LONG).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                adapter.notifyDataSetChanged();
                                break;
                        }


                    }
                };

                TextView myMsg = new TextView(ShoppingListAddEditActivity.this);
                myMsg.setText("\nSure you want to delete \n\n" + adapter.getShoppingListRowAt(viewHolder.getAdapterPosition()).getProductName() + "?" );
                myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                myMsg.setTextColor(Color.BLACK);
                myMsg.setTextSize(15);

                AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingListAddEditActivity.this);
                builder.setView(myMsg).setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


            }
        }).attachToRecyclerView(recyclerView);

    }



    public void saveList(View v)
    {

        if (adapter.getItemCount() == 0)
        {
            Toast.makeText(ShoppingListAddEditActivity.this, "List is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (listId != -1)
        {
            UpdateRowsListAdd();
            Toast.makeText(ShoppingListAddEditActivity.this, "Hurray", Toast.LENGTH_SHORT).show();

            finish();
            return;
        }


        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_save_list, null);
        final EditText input = (EditText) view.findViewById(R.id.etComments);

        AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingListAddEditActivity.this);
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

                            ShoppingList shoppingList = new ShoppingList(listName, sListType, date);

                            insertedListId = (int) shoppingListViewModel.insert(shoppingList);
                            UpdateRowsListAdd();

                            Toast.makeText(ShoppingListAddEditActivity.this, "Hurray", Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();

                            Intent intentPreviewList = new Intent(ShoppingListAddEditActivity.this, ShoppingListListActivity.class);
                            startActivity(intentPreviewList);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(ShoppingListAddEditActivity.this, "Enter list Name", Toast.LENGTH_SHORT).show();
                        }



                    }
                });

            }
        });

        alertDialog.show();

        updateDBList();
    }


    public void UpdateRowsListAdd()
    {
        for(ShoppingListRow shoppingListRow : adapter.getShoppingListRows())
        {

            if(listId == -1)
            {
                shoppingListRow.setFlag(0);
                shoppingListRow.setShoppingListId(insertedListId);

                shoppingListViewModel.update(shoppingListRow);
            }
            else
            {
                shoppingListRow.setFlag(0);
                shoppingListRow.setQnt(shoppingListRow.getTmpQnt());
                shoppingListViewModel.update(shoppingListRow);
            }

        }
        adapter.notifyDataSetChanged();
    }

    public void addFromRecipes(View v)
    {

        Intent intent = new Intent(ShoppingListAddEditActivity.this, ShoppingListAddRecipeActivity.class);

        List<ShoppingListRow> list = new ArrayList<>();
        list = adapter.getShoppingListRows();

        intent.putExtra("LIST",(Serializable) list);
        intent.putExtra(ShoppingListAddProductActivity.EXTRA_LIST_ID,listId);
        startActivityForResult(intent,ADD_LIST_RECIPE_REQUEST);
        updateDBList();

    }
    public void addFromProducts(View v)
    {
        Intent intent = new Intent(ShoppingListAddEditActivity.this, ShoppingListAddProductActivity.class);

        List<ShoppingListRow> list = new ArrayList<>();
        list = adapter.getShoppingListRows();

        intent.putExtra("LIST",(Serializable) list);
        intent.putExtra(ShoppingListAddProductActivity.EXTRA_LIST_ID,listId);
        startActivityForResult(intent,ADD_LIST_PRODUCT_REQUEST);
        updateDBList();

    }

    public void updateDBList()
    {
        for(ShoppingListRow shoppingListRow : adapter.getShoppingListRows())
        {
            shoppingListViewModel.update(shoppingListRow);
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed()
    {
        updateDBList();
        finish();

    }


}