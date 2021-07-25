package com.example.shoppinglistapp;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_Meals")
public class Meal
{
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String MealName;
    private String MealDescription;
    private int qnt = 1;

    public int getQnt() {
        return qnt;
    }

    public void setQnt(int qnt) {
        this.qnt = qnt;
    }

    public Meal(){}

    @Ignore
    public Meal(String mealName, String mealDescription) {
        MealName = mealName;
        MealDescription = mealDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMealName() {
        return MealName;
    }

    public void setMealName(String mealName) {
        MealName = mealName;
    }

    public String getMealDescription() {
        return MealDescription;
    }

    public void setMealDescription(String mealDescription) {
        MealDescription = mealDescription;
    }






}
