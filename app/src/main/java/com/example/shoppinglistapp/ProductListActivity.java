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

public class ProductListActivity extends AppCompatActivity {
    public static final int ADD_PRODUCT_REQUEST = 1;
    public static final int EDIT_PRODUCT_REQUEST = 2;

    EditText editTextSearchPr;


    private ProductViewModel productViewModel;
    public ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        //getWindow().setBackgroundDrawable(getResources().getDrawable(R.mipmap.blue2)); ;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        FloatingActionButton buttonAddProduct = findViewById(R.id.button_add_product);
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductListActivity.this, ProductAddEditActivity.class);
                startActivityForResult(intent,ADD_PRODUCT_REQUEST);

            }


        });

        editTextSearchPr = findViewById(R.id.edittext_search_product);


        editTextSearchPr.addTextChangedListener(new TextWatcher()
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

        RecyclerView recyclerView = findViewById(R.id.recycler_view_product);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ProductAdapter();

        recyclerView.setAdapter((adapter));


        productViewModel = new ViewModelProvider(this
                ,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(ProductViewModel.class);

        productViewModel.getAllProducts().observe(this, new Observer<List<Product>>()
        {
            @Override
            public void onChanged(List<Product> products)
            {
                //update RecyclerView

                adapter.setProducts(products);
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

                                productViewModel.delete(adapter.getProductAt(viewHolder.getAdapterPosition()));
                                Toast.makeText(ProductListActivity.this, "Product deleted", Toast.LENGTH_LONG).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                adapter.notifyDataSetChanged();
                                break;
                        }


                    }
                };

                TextView myMsg = new TextView(ProductListActivity.this);
                myMsg.setText("\nSure you want to delete \n\n" + adapter.getProductAt(viewHolder.getAdapterPosition()).getName() + "?" );
                myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                myMsg.setTextColor(Color.BLACK);
                myMsg.setTextSize(15);

                AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this);
                builder.setView(myMsg).setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnProductListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(ProductListActivity.this, ProductAddEditActivity.class);
                intent.putExtra(ProductAddEditActivity.EXTRA_NAME,product.getName());
                intent.putExtra(ProductAddEditActivity.EXTRA_UNITS,product.getUnit());
                intent.putExtra(ProductAddEditActivity.EXTRA_ID,product.getId());
                Log.d("EDIT", "LIST: " + ProductAddEditActivity.EXTRA_UNITS + ProductAddEditActivity.EXTRA_NAME);
                startActivityForResult(intent, EDIT_PRODUCT_REQUEST);



            }
        });

    }

    public void filter(String text)
    {

        productViewModel.getAllProducts().observe(this, new Observer<List<Product>>()
        {
            @Override
            public void onChanged(List<Product> products)
            {
                //update RecyclerView
                adapter.setProducts(products);
                //Toast.makeText(ProductListActivity.this, "Use + to add a new product",Toast.LENGTH_LONG).show();
            }
        });

        Log.d("test", String.valueOf(adapter.getItemCount()));


        List<Product> filteredProducts = new ArrayList<>();
        List<Product> inputProducts = adapter.getProducts();
        Log.d("test", String.valueOf(adapter.getItemCount()));

        for (Product product : inputProducts)
        {
            if (product.getName().toLowerCase().contains(text.toLowerCase()))
            {
                filteredProducts.add(product);
            }
        }

        adapter.setProducts(filteredProducts);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        editTextSearchPr = findViewById(R.id.edittext_search_product);

        if(requestCode == ADD_PRODUCT_REQUEST && resultCode == RESULT_OK)
        {
            String name = data.getStringExtra(ProductAddEditActivity.EXTRA_NAME);
            String unit = data.getStringExtra(ProductAddEditActivity.EXTRA_UNITS);

            Product product = new Product(name,unit, 0);
            productViewModel.insert(product);

            Toast.makeText(this,"Product added", Toast.LENGTH_SHORT).show();

            editTextSearchPr.setText("");


        }
        else if(requestCode == EDIT_PRODUCT_REQUEST && resultCode == RESULT_OK)
        {
            int id = data.getIntExtra(ProductAddEditActivity.EXTRA_ID, -1);

            if(id == -1)
            {
                Toast.makeText(this,"Product can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = data.getStringExtra(ProductAddEditActivity.EXTRA_NAME);
            String unit = data.getStringExtra(ProductAddEditActivity.EXTRA_UNITS);

            Product product = new Product(name,unit, 0);
            product.setId(id);
            productViewModel.update(product);

            Toast.makeText(this,"Product updated",Toast.LENGTH_SHORT).show();
            editTextSearchPr.setText("");
        }
        else
        {
            if(requestCode == ADD_PRODUCT_REQUEST){
                Toast.makeText(this,"Product not added", Toast.LENGTH_SHORT).show();
            }


        }


    }
}