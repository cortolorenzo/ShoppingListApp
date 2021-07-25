package com.example.shoppinglistapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ShoppingListListAdapter extends RecyclerView.Adapter<ShoppingListListAdapter.ShoppingListListHolder> {

    private ShoppingList shoppingList;
    private List<ShoppingList> shoppingLists = new ArrayList<>();
    private ShoppingListListAdapter.OnItemClickListener listener;
    private MealAdapter.OnItemLongClickListener listenerLong;

    @NonNull
    @Override
    public ShoppingListListAdapter.ShoppingListListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_list_item, parent,false);

        return new ShoppingListListAdapter.ShoppingListListHolder((itemView));


    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListListAdapter.ShoppingListListHolder holder, int position) {

        ShoppingList currentList = shoppingLists.get(position);

        holder.textViewTitle.setText(currentList.getTitle());
        holder.textViewType.setText(currentList.getType());

        Date date = currentList.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("(EEE) dd-MM-yyyy", Locale.US);
        String strDate = dateFormat.format(date);
        holder.textViewDate.setText(strDate);

    }

    @Override
    public int getItemCount() {

        return shoppingLists.size();
    }

    public List<ShoppingList> getShoppingLists()
    {
        return this.shoppingLists;

    }


    public void setShoppingList(List<ShoppingList> shoppingLists){

        this.shoppingLists = shoppingLists;
        notifyDataSetChanged();

    }

    public ShoppingList getShoppingListAt(int position)
    {
        return shoppingLists.get(position);
    }


    class ShoppingListListHolder extends  RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewDate;
        private TextView textViewType;



        public ShoppingListListHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_lTitle);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewType = itemView.findViewById(R.id.text_view_type);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(shoppingLists.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(ShoppingList shoppingList);
    }

    public void setOnMealListener(ShoppingListListAdapter.OnItemClickListener listener)
    {
        this.listener = listener;
    }



}
