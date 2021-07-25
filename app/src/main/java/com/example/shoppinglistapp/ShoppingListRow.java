package com.example.shoppinglistapp;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "tbl_ShoppingListRow")
public class ShoppingListRow implements Serializable
{

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int shoppingListId;
    private int productId;
    private double qnt;
    private double tmpQnt = 0;
    private int isBought = 0;
    private int flag;

// query additional fields
    private String productName;
    private String Units;

    public double getTmpQnt() {
        return tmpQnt;
    }

    public void setTmpQnt(double tmpQnt) {
        this.tmpQnt = tmpQnt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(int shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getQnt() {
        return qnt;
    }

    public void setQnt(double qnt) {
        this.qnt = qnt;
    }

    public int getIsBought() {
        return isBought;
    }

    public void setIsBought(int isBought) {
        this.isBought = isBought;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnits() {
        return Units;
    }

    public void setUnits(String units) {
        Units = units;
    }

    @Ignore
    public ShoppingListRow(int shoppingListId, int productId, double qnt, int isBought, int flag) {
        this.shoppingListId = shoppingListId;
        this.productId = productId;
        this.qnt = qnt;
        this.isBought = isBought;
        this.flag = flag;
    }

    public ShoppingListRow(){}

}
