package com.samsung.drawshapes.db;

public class MyColor {

    public int id;
    public String colorName;
    public int colorValue;
    public int currentSelectedColor;

    public  MyColor(int id, String name, int colorValue, int currentSelectedColor){
        this.id = id;
        this.colorName = name;
        this.colorValue = colorValue;
        this.currentSelectedColor = currentSelectedColor;


    }


    public  String  getColorName(){
        return colorName;
    }
    public int getColorValue(){
        return colorValue;
    }
    public int getId(){
        return id;
    }







}
