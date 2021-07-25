package com.example.shoppinglistapp;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "tbl_MealsSchedule_Row")
public class MealScheduleRow
{
    @PrimaryKey(autoGenerate = true)
    private int MealScheduleRow_id;

    private int MealSchedule_id;
    private int MealId;
    private int MealQnt = 1;


    public String getMealScheduleRowDesc() {
        return MealScheduleRowDesc;
    }

    public void setMealScheduleRowDesc(String mealScheduleRowDesc) {
        MealScheduleRowDesc = mealScheduleRowDesc;
    }

    public String getMealScheduleRowName() {
        return MealScheduleRowName;
    }

    public void setMealScheduleRowName(String mealScheduleRowName) {
        MealScheduleRowName = mealScheduleRowName;
    }

    private String MealScheduleRowDesc;
    private String MealScheduleRowName;


    @Ignore
    public MealScheduleRow(int mealSchedule_id, int mealId, int mealQnt) {
        MealSchedule_id = mealSchedule_id;
        MealId = mealId;
        MealQnt = mealQnt;
    }

    public MealScheduleRow(){}



    public int getMealScheduleRow_id() {
        return MealScheduleRow_id;
    }

    public void setMealScheduleRow_id(int mealScheduleRow_id) {
        MealScheduleRow_id = mealScheduleRow_id;
    }

    public int getMealSchedule_id() {
        return MealSchedule_id;
    }

    public void setMealSchedule_id(int mealSchedule_id) {
        MealSchedule_id = mealSchedule_id;
    }

    public int getMealId() {
        return MealId;
    }

    public void setMealId(int mealId) {
        MealId = mealId;
    }

    public int getMealQnt() {
        return MealQnt;
    }

    public void setMealQnt(int mealQnt) {
        MealQnt = mealQnt;
    }





}
