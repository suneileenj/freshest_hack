package com.example.madeline.fridgefinder;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Calendar;
import java.util.Date;

public class FoodEntry {
    //instance variables
    String foodName;
    String dateAdded;
    String dateExpiry;
    public FoodEntry(String foodName, String date, int days){
        this.setFoodName(foodName);
        this.setDateAdded(date);
        this.setExpiry(days);
    }
    /**
     * setter for foodname
     * @param name the name of the food
     * */
    protected void setFoodName(String name){
        this.foodName = name;
    }
    /**
     * setter for dateExpiry
     * @param days the days until expiry
     * */
    protected void setExpiry(int days){
        Calendar date = Calendar.getInstance();
        date.setTime(new Date()); //today's date
        date.add(Calendar.DATE, days);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
        this.dateExpiry = dateFormat.format(date.getTime());

    }

    /**
     * setter for date
     * */
    protected void setDateAdded(String date){
        this.dateAdded = date;
    }

    /**
     * gets the current date
     * @return the current date in a string
     * */
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected static String currentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
        return dateFormat.format(new Date());
    }

}
