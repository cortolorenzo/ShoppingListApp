package com.example.shoppinglistapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListAddProductActivity extends AppCompatActivity {
    public static final String EXTRA_LIST_ID =
            "com.example.shoppinglistapp.EXTRA_LIST_ID";

    public static final String EXTRA_PRODUCT_ID =
            "com.example.shoppinglistapp.EXTRA_PRODUCT_ID";

    public static final String CZY_EDIT =
            "com.example.shoppinglistapp.CZY_EDIT";

    private ShoppingListViewModel shoppingListViewModel;

    EditText editTextSearchPr;
    private int listId;
    public ProductAdapter adapter;
    private ProductViewModel productViewModel;
    private ArrayList<MealRow> productsArray;
    boolean czyEdit;
    public static final int ADD_PRODUCT_REQUEST = 1;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        setTitle("Select product to add");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intent = getIntent();
        listId = intent.getIntExtra(EXTRA_LIST_ID, 0);
        Log.e("edit mode", String.valueOf(listId));
        if( listId == -1)
        {
            czyEdit = false;
        }
        else
        {
            czyEdit = true;
        }



        shoppingListViewModel = new ViewModelProvider(this
                , ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(ShoppingListViewModel.class);




        FloatingActionButton buttonAddProduct = findViewById(R.id.button_add_product);
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingListAddProductActivity.this, ProductAddEditActivity.class);
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
                filter(editable.toString() );
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recycler_view_product);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ProductAdapter();
        adapter.mealProductsArr = productsArray;

        recyclerView.setAdapter((adapter));


        productViewModel = new ViewModelProvider(this
                ,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(ProductViewModel.class);


        productViewModel.getAllProducts().observe(ShoppingListAddProductActivity.this, new Observer<List<Product>>()
        {
            @Override
            public void onChanged(List<Product> products)
            {
                adapter.setProducts(products);
            }
        });


        adapter.setOnProductListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                //Toast.makeText(MealProductAddActivity.this, "test"+ product.getName(), Toast.LENGTH_SHORT).show();

                int id_product = product.getId();

                Intent i = getIntent();

                for(ShoppingListRow shoppingListRow : (List<ShoppingListRow>) i.getSerializableExtra("LIST"))
                {
                    if (shoppingListRow.getProductId() == id_product)
                    {
                        Toast.makeText(ShoppingListAddProductActivity.this, "Product already on a list", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Intent data = new Intent();
                data.putExtra(EXTRA_PRODUCT_ID, id_product);
                Log.d("mealID ", String.valueOf(listId));
                Log.d("productID ", String.valueOf(id_product));
                Log.d("czyedit ", String.valueOf(czyEdit));

                setResult(RESULT_OK,data);

                ShoppingListRow shoppingListRow;
                if (czyEdit == false)
                {
                    shoppingListRow = new ShoppingListRow(-1, id_product, 1, 0, 1);

                }
                else
                {
                    shoppingListRow = new ShoppingListRow(listId, id_product, 1, 0, 1);
                    shoppingListRow.setTmpQnt(shoppingListRow.getQnt());
                }
                shoppingListViewModel.insert(shoppingListRow);


                finish();


            }
        });

    }

    public void filter(String text)
    {
        productViewModel.getAllProducts().observe(ShoppingListAddProductActivity.this, new Observer<List<Product>>()
        {
            @Override
            public void onChanged(List<Product> products)
            {
                adapter.setProducts(products);

            }
        });

        List<Product> filteredProducts = new ArrayList<>();
        List<Product> inputProducts = adapter.getProducts();

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

        if (requestCode == ADD_PRODUCT_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(ProductAddEditActivity.EXTRA_NAME);
            String unit = data.getStringExtra(ProductAddEditActivity.EXTRA_UNITS);

            Product product = new Product(name, unit, 0);
            productViewModel.insert(product);

            Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();

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