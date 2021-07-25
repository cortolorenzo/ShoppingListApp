package com.example.shoppinglistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListDetailsActivity extends AppCompatActivity
{


    public static final String SHOPPING_LIST_ID = "com.example.shoppinglistapp.SHOPPING_LIST_ID";
    public static final String MODE_SHOPPING_lIST_TYPE = "com.example.shoppinglistapp.MODE_SHOPPING_lIST_TYPE";
    public static final int EDIT_LIST_REQUEST = 1;

    ShoppingListViewModel shoppingListViewModel;
    ShoppingListEditAdapter adapter;

    private String sListType;
    private int listType;
    private int listId;

    private CardView cardViewItem;

    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;

    FloatingActionButton buttonEditList;
    FloatingActionButton buttonMenuList;
    FloatingActionButton buttonShareList;
    private boolean clicked = false;

    private void onButtonClicked()
    {
        setVisibility(clicked);
        setAnimation(clicked);
        setClickable(clicked);
        if(!clicked) clicked = true; else clicked = false;

    }

    private void setVisibility(boolean clicked)
    {
        if(!clicked)
        {
            buttonEditList.setVisibility(View.VISIBLE);
            buttonShareList.setVisibility(View.VISIBLE);
        }
        else
        {
            buttonEditList.setVisibility(View.INVISIBLE);
            buttonShareList.setVisibility(View.INVISIBLE);
        }

    }

    private void setClickable(boolean clicked)
    {
        if(!clicked)
        {
            buttonEditList.setClickable(true);
            buttonShareList.setClickable(true);
        }
        else
        {
            buttonEditList.setClickable(false);
            buttonShareList.setClickable(false);
        }
    }

    private void setAnimation(boolean clicked)
    {
        if(!clicked)
        {
            buttonMenuList.startAnimation(rotateOpen);
            buttonEditList.startAnimation(fromBottom);
            buttonShareList.startAnimation(fromBottom);
        }
        else
        {
            buttonMenuList.startAnimation(rotateClose);
            buttonEditList.startAnimation(toBottom);
            buttonShareList.startAnimation(toBottom);
        }

    }





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        cardViewItem = findViewById(R.id.meal_product_item);

        rotateOpen = AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim);

        buttonEditList = findViewById(R.id.button_edit_list);
        buttonMenuList = findViewById(R.id.button_menu_list);
        buttonShareList = findViewById(R.id.button_share_list);

        buttonMenuList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClicked();
            }
        });


        buttonShareList.setOnClickListener(new View.OnClickListener()
        {



            @Override
            public void onClick(View v)
            {

                if (cntSelected() == 0)
                {
                    shareShoppingList(false);
                    return;
                }


                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        switch (which)
                        {
                            case DialogInterface.BUTTON_POSITIVE:
                                shareShoppingList(false);

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                shareShoppingList(true);
                                break;
                        }
                    }
                };



                TextView myMsg = new TextView(ShoppingListDetailsActivity.this);
                myMsg.setText("\nWhich products would you like to send? \n" );
                myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                myMsg.setTextColor(Color.BLACK);
                myMsg.setTextSize(15);

                AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingListDetailsActivity.this);
                builder.setView(myMsg).setPositiveButton("All", dialogClickListener)
                        .setNegativeButton("Not selected", dialogClickListener)

                        .show();
                onButtonClicked();


            }
        });




        buttonEditList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ShoppingListDetailsActivity.this, ShoppingListAddEditActivity.class);
                intent.putExtra(ShoppingListAddEditActivity.MODE_ADD_SHOPPING_lIST, 0); // 1 - add, 0 - edit
                intent.putExtra(ShoppingListAddEditActivity.MODE_SHOPPING_lIST_TYPE, listType); // 0 - Scheduler, 1 - Quick List
                intent.putExtra(ShoppingListAddEditActivity.SHOPPING_LIST_ID, listId); // 0 - Scheduler, 1 - Quick List
                UpdateTmpQnt();
                onButtonClicked();
                startActivityForResult(intent,EDIT_LIST_REQUEST);

            }


        });


        RecyclerView recyclerView = findViewById(R.id.recycler_view_list_details);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ShoppingListEditAdapter();
        adapter.listPreviewMode = true;
        adapter.shoppingListDetailsActivity = this;
        recyclerView.setAdapter((adapter));

        shoppingListViewModel = new ViewModelProvider(this
                ,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(ShoppingListViewModel.class);


// Caller source identification
        Intent intent = getIntent();
        listId = intent.getIntExtra(SHOPPING_LIST_ID, 0);
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


        shoppingListViewModel.getAllShoppingListRows(listId).observe(this, new Observer<List<ShoppingListRow>>()
        {
            @Override
            public void onChanged(List<ShoppingListRow> shoppingListRows)
            {
                Log.e("query", String.valueOf(listId));
                adapter.setShoppingListRows(shoppingListRows);
                UpdateTmpListRows();
            }
        });

//Deleting tmp rows created and not saved in edit mode



        adapter.setOnMealListener(new ShoppingListEditAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(ShoppingListRow shoppingListRow)
            {
                if (shoppingListRow.getIsBought() == 0)
                {
                    shoppingListRow.setIsBought(1);
                    Toast.makeText(ShoppingListDetailsActivity.this, "Bravo!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    shoppingListRow.setIsBought(0);
                }
                shoppingListViewModel.update(shoppingListRow);
                adapter.notifyDataSetChanged();
            }
        });



    }

    int cntSelected()
    {
        int retval = 0;
        for (ShoppingListRow shoppingListRow : adapter.getShoppingListRows())
        {
            if (shoppingListRow.getIsBought() == 1)
            {
                retval++;
            }
        }


        return retval;
    }

    void shareShoppingList(boolean onlyNotBght)
    {
        String shoppingList = "";

        int cnt = 0;

        List<ShoppingListRow> productsToShare = new ArrayList<>();
        for(ShoppingListRow shoppingListRow : adapter.getShoppingListRows())
        {
            if(onlyNotBght)
            {
                if(shoppingListRow.getIsBought() == 0)
                {
                    productsToShare.add(shoppingListRow);
                }
            }
            else
            {
                productsToShare.add(shoppingListRow);
            }
        }


        for(ShoppingListRow shoppingListRow : productsToShare)
        {
            String name = shoppingListRow.getProductName();

            Double dQnt = shoppingListRow.getQnt();
            DecimalFormat format = new DecimalFormat("0.###");

            String qnt = String.valueOf(format.format(dQnt))
                    .replace(",",".");

            String units = String.valueOf(shoppingListRow.getUnits());

            if (cnt == 0)
            {
                shoppingList = name + ", " + qnt + " " + units;
            }
            else
            {
                shoppingList = shoppingList + "\n" + name + ", " + qnt + " " + units;
            }
            cnt++;
        }


        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shoppingList);
        shareIntent.setType("text/plain");
        startActivity(shareIntent);

    }

    void UpdateTmpQnt()
    {
        Log.e("TEST", String.valueOf(adapter.getItemCount()));
        for (ShoppingListRow shoppingListRow : adapter.getShoppingListRows())
        {
            shoppingListRow.setTmpQnt(shoppingListRow.getQnt());
            shoppingListViewModel.update(shoppingListRow);
        }
        adapter.notifyDataSetChanged();
    }



    void UpdateTmpListRows()
    {
        Log.e("TEST", String.valueOf(adapter.getItemCount()));
        for (ShoppingListRow shoppingListRow : adapter.getShoppingListRows())
        {
            if (shoppingListRow.getFlag() == 1 && shoppingListRow.getShoppingListId() != -1)
            {
                shoppingListViewModel.delete(shoppingListRow);
            }
            if(shoppingListRow.getFlag() == 2)
            {
                shoppingListRow.setFlag(0);
                shoppingListViewModel.update(shoppingListRow);
            }

        }
        adapter.notifyDataSetChanged();
    }
}