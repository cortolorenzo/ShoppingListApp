package com.example.shoppinglistapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface MealScheduleDao
{

    //MealScheduleRow

    @Insert
    long insert(MealScheduleRow mealScheduleRow);

    @Update
    void update(MealScheduleRow mealScheduleRow);

    @Delete
    void delete(MealScheduleRow mealScheduleRow);

    @Query("SELECT * FROM tbl_MealsSchedule_Row WHERE MealSchedule_id = :mealScheduleId  ORDER BY MealScheduleRow_id ASC")
    LiveData<List<MealScheduleRow>> getMealScheduleRows(int mealScheduleId);

    @Query("SELECT MSR.MealScheduleRow_id, MSR.MealSchedule_id, MSR.MealId, MSR.MealQnt," +
            " M.MealDescription 'MealScheduleRowDesc', M.MealName 'MealScheduleRowName' " +
            " FROM tbl_MealsSchedule_Row MSR INNER JOIN tbl_Meals M ON MSR.MealId = M.id" +
            " ORDER BY 1 ASC ")
    LiveData<List<MealScheduleRow>> getMealsForSchedule();


    // MealSchedule

    @Insert
    long insert(MealSchedule mealSchedule);

    @Update
    void update(MealSchedule mealSchedule);

    @Delete
    void delete(MealSchedule mealSchedule);

    @Query("SELECT MealSchedule_id, scheduleDate, isSelected FROM tbl_MealsSchedule " +
            "WHERE scheduleDate BETWEEN :startDate AND :endDate " )
    LiveData<List<MealSchedule>> getAllMealSchedules(java.util.Date startDate, java.util.Date endDate);







}
