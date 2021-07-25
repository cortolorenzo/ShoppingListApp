package com.example.shoppinglistapp;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_Products")
public class Product {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String unit;

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getFlag() {
        return Flag;
    }

    public void setFlag(int flag) {
        Flag = flag;
    }

    private int Flag =0;

    public Product(){}

    @Ignore
    public Product(String name, String unit)
    {
        this.name = name;
        this.unit = unit;

    }

    @Ignore
    public Product(String name, String unit, int flag)
    {
        this.name = name;
        this.unit = unit;
        this.Flag = flag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }
}
