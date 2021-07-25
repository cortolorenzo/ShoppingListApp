package com.example.shoppinglistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static android.content.Intent.EXTRA_TITLE;

public class ProductAddEditActivity extends AppCompatActivity {
    public static final String EXTRA_NAME =
            "com.example.shoppinglistapp.EXTRA_NAME";
    public static final String EXTRA_UNITS =
            "com.example.shoppinglistapp.EXTRA_UNITS";
    public static final String EXTRA_ID =
            "com.example.shoppinglistapp.EXTRA_ID";

    private EditText editTextName;
    private Spinner spinner;
    //private String


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //get the spinner from the xml.
        spinner = findViewById(R.id.spinner_units);
        //create a list of items for the spinner.
        String[] items = new String[]{"pcs.", "kg", "g", "l", "ml"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        spinner.setAdapter(adapter);


        editTextName = findViewById(R.id.edit_text_name);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID))
        {
            setTitle("Edit Product");
            editTextName.setText(intent.getStringExtra(EXTRA_NAME));

            Log.d("Add", "Units: " + EXTRA_UNITS);
            spinner.setSelection(adapter.getPosition(intent.getStringExtra(EXTRA_UNITS)));
        }
        else
        {
            setTitle("Define new product");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_product_menu,menu);

        return true;
    }

    public void setSpinText(Spinner spin, String text)
    {
        for(int i= 0; i < spin.getAdapter().getCount(); i++)
        {
            if(spin.getAdapter().getItem(i).toString().contains(text))
            {
                spin.setSelection(i);
            }
        }

    }

    private void saveNote()
    {
        String name = editTextName.getText().toString();
        String unit = spinner.getSelectedItem().toString();

        Log.d("Add", "Units: " + unit);

        if (name.trim().isEmpty() || unit.trim().isEmpty())
        {
            Toast.makeText(this,"Please insert Name and its units", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_UNITS, unit);

        int id = getIntent().getIntExtra(EXTRA_ID,-1);
        if (id != -1)
        {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK,data);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch  (item.getItemId())
        {
            case R.id.save_product:
                saveNote();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}