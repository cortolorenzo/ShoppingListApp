package com.example.shoppinglistapp;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.Date;


@Entity(tableName = "tbl_MealsSchedule")
public class MealSchedule
{

    public int getMealSchedule_id() {
        return MealSchedule_id;
    }

    public void setMealSchedule_id(int mealSchedule_id) {
        MealSchedule_id = mealSchedule_id;
    }

    @PrimaryKey(autoGenerate = true)
    private int MealSchedule_id;

    private Date scheduleDate;
    private int isSelected = 0;

    public MealSchedule(){}

    @Ignore
    public MealSchedule(Date date)
    {
        scheduleDate = date;
    }

    public int getId() {
        return MealSchedule_id;
    }

    public void setId(int id) {
        this.MealSchedule_id = id;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }
}


