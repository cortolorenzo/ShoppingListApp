package com.example.shoppinglistapp;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;


@Entity(tableName = "tbl_Meals_Row")
public class MealRow implements Serializable
{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int MealId;
    private int ProductId;
    private double Quantity;

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductUnit() {
        return ProductUnit;
    }

    public void setProductUnit(String productUnit) {
        ProductUnit = productUnit;
    }

    private String ProductName;
    private String ProductUnit;

    public int getInsertFlag() {
        return insertFlag;
    }

    public void setInsertFlag(int insertFlag) {
        this.insertFlag = insertFlag;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    private int insertFlag = 0;
    private int deleteFlag = 0;

    @Ignore
    public MealRow(int mealId, int productId, double quantity) {
        MealId = mealId;
        ProductId = productId;
        Quantity = quantity;
    }


    MealRow(){}



    public int getMealId() {
        return MealId;
    }

    public void setMealId(int mealId) {
        MealId = mealId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public double getQuantity() {
        return Quantity;
    }

    public void setQuantity(double quantity) {
        Quantity = quantity;
    }





}
