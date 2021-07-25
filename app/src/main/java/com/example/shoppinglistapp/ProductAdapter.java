package com.example.shoppinglistapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.telephony.ClosedSubscriberGroupInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    private List<Product> products = new ArrayList<>();
    private OnItemClickListener listener;
    public ArrayList<MealRow> mealProductsArr = null;


    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent,false);

        return new ProductHolder((itemView));


    }



    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {

        Product currentProduct = products.get(position);
        holder.textViewTitle.setText(currentProduct.getName());
        holder.textViewDescription.setText(currentProduct.getUnit());

 /*       if(currentProduct.getFlag() == 1)
        {
            holder.cardViewProduct.setCardBackgroundColor(Color.parseColor("#BF20B2AA"));
        }*/


        /*if (mealrow.getProductId() == product.getId())
        {
            holder.cardViewProduct.setCardBackgroundColor(Color.parseColor("#BF20B2AA"));
        }*/
/*
        for (MealRow product : mealProductsArr)
        {
            Log.d("TEST","petla: " + product.getProductId() + String.valueOf(currentProduct.getId()));
        }*/

   /*     if (mealProductsArr != null)
        {
            for (MealRow product : mealProductsArr)
            {
                if (product.getProductId() == currentProduct.getId())
                {
                    holder.cardViewProduct.setCardBackgroundColor(Color.parseColor("#BF20B2AA"));
                }
            }
        }*/


/*        if (currentProduct.getFlag() == 1)
        {
            holder.cardViewProduct.setCardBackgroundColor(Color.parseColor("#BF20B2AA"));
        }
        Log.d("test","test");*/

        //holder.textViewPriority.setText(String.valueOf(currentProduct.getId()));



    }

    @Override
    public int getItemCount() {

        return products.size();
    }

    public List<Product> getProducts()
    {

        return this.products;

    }

    public void setProducts(List<Product> products){

        this.products = products;
        notifyDataSetChanged();
    }

    public Product getProductAt(int position)
    {
        return products.get(position);
    }


    class ProductHolder extends  RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;
        private CardView cardViewProduct;


        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_prority);
            cardViewProduct = itemView.findViewById(R.id.product_card_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                    listener.onItemClick(products.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(Product product);
    }

    public void setOnProductListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

}
