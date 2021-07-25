package com.example.shoppinglistapp;

import android.app.Application;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DecimalFormat;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class MealRowAdapter extends RecyclerView.Adapter<MealRowAdapter.MealRowHolder>
{
    private List<MealRow> mealProducts = new ArrayList<>();
    private List<MealRow> mealFlagged = new ArrayList<>();
    private MealRowAdapter.OnItemClickListener listener;
    private MealRepository repository;

    public boolean scheduleAcivityRequest = false;




    @NonNull
    @Override
    public MealRowAdapter.MealRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_product_item, parent,false);



        return new MealRowAdapter.MealRowHolder((itemView));


    }

    @Override
    public void onBindViewHolder(@NonNull MealRowHolder holder, int position) {

        MealRow currentMealRow = mealProducts.get(position);

        DecimalFormat format = new DecimalFormat("0.###");
        Double dQnt = currentMealRow.getQuantity();

        String strQnt = String.valueOf(format.format(dQnt))
                .replace(",",".");

        holder.editTextQuantity.setText(strQnt);
        holder.textViewProductUnits.setText(currentMealRow.getProductUnit());
        holder.textViewProductName.setText(currentMealRow.getProductName());

        holder.editTextQuantity.setTag(position);

        if (scheduleAcivityRequest)
        {
            holder.editTextQuantity.setFocusable(false);
            holder.editTextQuantity.setFocusableInTouchMode(false);
            holder.editTextQuantity.setInputType(InputType.TYPE_NULL);
        }

    }

    @Override
    public int getItemCount() {

        return mealProducts.size();
    }

    public List<MealRow> getMealProducts()
    {
        return this.mealProducts;

    }

    public List<MealRow> getMealsFlagged()
    {
        return this.mealFlagged;

    }

    public void setMealFlagged(List<MealRow> mealProducts){

        this.mealFlagged = mealProducts;


    }



    public void setMealProducts(List<MealRow> mealProducts){

        this.mealProducts = mealProducts;
        notifyDataSetChanged();

    }

    public MealRow getMealProductAt(int position)
    {
        return mealProducts.get(position);
    }


    class MealRowHolder extends  RecyclerView.ViewHolder{

        private TextView textViewProductName;
        public EditText editTextQuantity;
        private TextView textViewProductUnits;

        public MealRowHolder(@NonNull View itemView) {
            super(itemView);

            textViewProductName = itemView.findViewById(R.id.text_view_meal_product_name);
            editTextQuantity = itemView.findViewById(R.id.text_view_meal_product_qnt);
            textViewProductUnits = itemView.findViewById(R.id.text_view_meal_product_units);



            editTextQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
                {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                {



                }

                @Override
                public void afterTextChanged(Editable editable)
                {
                    try
                    {
                        if (editTextQuantity.getText().toString().length() > 0)
                        {


                            if(editTextQuantity.getTag()!=null)
                            {
                                mealProducts.get(getLayoutPosition()).setQuantity(Double.parseDouble(editTextQuantity.getText().toString()));

                            }
                        }
                    } catch (NumberFormatException e)
                    {
                        e.printStackTrace();
                    }


                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(mealProducts.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(MealRow mealRow);
    }

    public void setOnMealListener(MealRowAdapter.OnItemClickListener listener)
    {
        this.listener = listener;
    }

}
