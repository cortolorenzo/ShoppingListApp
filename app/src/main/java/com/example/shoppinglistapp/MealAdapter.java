package com.example.shoppinglistapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealHolder> {

    private Meal meal;
    private List<Meal> meals = new ArrayList<>();
    private MealAdapter.OnItemClickListener listener;
    private MealAdapter.OnItemLongClickListener listenerLong;

    @NonNull
    @Override
    public MealAdapter.MealHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_item, parent,false);

        return new MealAdapter.MealHolder((itemView));


    }

    @Override
    public void onBindViewHolder(@NonNull MealAdapter.MealHolder holder, int position) {

        Meal currentMeal = meals.get(position);
        holder.textViewTitle.setText(currentMeal.getMealName());
        holder.textViewDescription.setText(currentMeal.getMealDescription());
        //holder.textViewPriority.setText(String.valueOf(currentProduct.getId()));



    }

    @Override
    public int getItemCount() {

        return meals.size();
    }

    public List<Meal> getMeals()
    {
        return this.meals;

    }

    public void setMeal(Meal meal){

        this.meal = meal;
        notifyDataSetChanged();

    }

    public Meal getMeal()
    {
        return this.meal;

    }


    public void setMeals(List<Meal> meals){

        this.meals = meals;
        notifyDataSetChanged();

    }

    public Meal getMealAt(int position)
    {
        return meals.get(position);
    }


    class MealHolder extends  RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewDescription;



        public MealHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_title_meal);
            textViewDescription = itemView.findViewById(R.id.text_view_description_meal);


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if(listenerLong != null && position != RecyclerView.NO_POSITION)
                        listenerLong.onItemLongClick(meals.get(position));
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(meals.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(Meal meal);
    }

    public void setOnMealListener(MealAdapter.OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public interface OnItemLongClickListener
    {
        void onItemLongClick(Meal meal);
    }

    public void setOnMeallongListener(MealAdapter.OnItemLongClickListener listener)
    {
        this.listenerLong = listener;
    }

}
