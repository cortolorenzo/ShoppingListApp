package com.example.shoppinglistapp;


import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListEditAdapter extends RecyclerView.Adapter<ShoppingListEditAdapter.ShoppingListEditHolder>
{
    private List<ShoppingListRow>  shoppingListRows = new ArrayList<>();

    private List<MealRow> mealFlagged = new ArrayList<>();

    private ShoppingListEditAdapter.OnItemClickListener listener;
    private ShoppingListRepository repository;

    public boolean listPreviewMode = false;
    public ShoppingListDetailsActivity shoppingListDetailsActivity;

    List<View> itemViewList = new ArrayList<>();
    public boolean isEdit;





    @NonNull
    @Override
    public ShoppingListEditAdapter.ShoppingListEditHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_product_item, parent,false);

        return new ShoppingListEditAdapter.ShoppingListEditHolder((itemView));


    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListEditAdapter.ShoppingListEditHolder holder, int position) {

        ShoppingListRow currentShoppingListRow = shoppingListRows.get(position);
        DecimalFormat format = new DecimalFormat("0.###");
        Double dQnt;

        if (isEdit)
        {
            dQnt = currentShoppingListRow.getTmpQnt();
        }
        else
        {
            dQnt = currentShoppingListRow.getQnt();
        }
        String strQnt = String.valueOf(format.format(dQnt))
                .replace(",",".");

        holder.editTextQuantity.setText(strQnt);
        holder.textViewProductUnits.setText(currentShoppingListRow.getUnits());
        holder.textViewProductName.setText(currentShoppingListRow.getProductName());

        holder.editTextQuantity.setTag(position);


        if (listPreviewMode)
        {
            holder.editTextQuantity.setFocusable(false);
            holder.editTextQuantity.setFocusableInTouchMode(false);
            holder.editTextQuantity.setInputType(InputType.TYPE_NULL);

            if (currentShoppingListRow.getIsBought() == 1)
            {
                holder.cardViewItem.setCardBackgroundColor((ContextCompat.getColor(shoppingListDetailsActivity,R.color.green)));
            }
            else
            {
                holder.cardViewItem.setCardBackgroundColor((ContextCompat.getColor(shoppingListDetailsActivity,R.color.white)));
            }

        }


    }

    @Override
    public int getItemCount() {

        return shoppingListRows.size();
    }

    public List<ShoppingListRow> getShoppingListRows()
    {
        return this.shoppingListRows;

    }

    public List<MealRow> getMealsFlagged()
    {
        return this.mealFlagged;

    }

    public void setMealFlagged(List<MealRow> mealProducts){

        this.mealFlagged = mealProducts;


    }



    public void setShoppingListRows(List<ShoppingListRow> shoppingListRows){

        this.shoppingListRows = shoppingListRows;
        notifyDataSetChanged();

    }

    public ShoppingListRow getShoppingListRowAt(int position)
    {
        return shoppingListRows.get(position);
    }


    class ShoppingListEditHolder extends  RecyclerView.ViewHolder{

        private TextView textViewProductName;
        public EditText editTextQuantity;
        private TextView textViewProductUnits;
        private CardView cardViewItem;


        public ShoppingListEditHolder(@NonNull View itemView) {
            super(itemView);

            textViewProductName = itemView.findViewById(R.id.text_view_meal_product_name);
            editTextQuantity = itemView.findViewById(R.id.text_view_meal_product_qnt);
            textViewProductUnits = itemView.findViewById(R.id.text_view_meal_product_units);
            cardViewItem = itemView.findViewById(R.id.meal_product_item);



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

                                if (isEdit)
                                {
                                    shoppingListRows.get(getLayoutPosition()).setTmpQnt(Double.parseDouble(editTextQuantity.getText().toString()));
                                }
                                else
                                {
                                    shoppingListRows.get(getLayoutPosition()).setQnt(Double.parseDouble(editTextQuantity.getText().toString()));
                                }


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
                        listener.onItemClick(shoppingListRows.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(ShoppingListRow shoppingListRow);
    }

    public void setOnMealListener(ShoppingListEditAdapter.OnItemClickListener listener)
    {
        this.listener = listener;
    }

}