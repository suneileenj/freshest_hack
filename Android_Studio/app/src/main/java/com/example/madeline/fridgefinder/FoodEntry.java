package com.example.madeline.fridgefinder;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Calendar;
import java.util.Date;

/**
 * class for handling each entry entered into
 * the list. Currently, each entry has
 * an associated name, date added, and date
 * of expiration.
 *
 * */
public class FoodEntry {
    //instance variables
    String foodName;
    String dateAdded;
    String dateExpiry;

    /**
     * constructor that creates a new entry
     * @param foodName the name of the food
     * @param date the date added
     * @param days the number of days until expiration
     * */
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
     * gets the current date and adds the days
     * to set the date of expiration
     * @param days the days until expiry
     * */
    protected void setExpiry(int days){
        Calendar date = Calendar.getInstance();
        date.setTime(new Date()); //today's date
        date.add(Calendar.DATE, days);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yy");
        this.dateExpiry = dateFormat.format(date.getTime());

    }

    /**
     * setter for date
     * @param date the date added
     * */
    protected void setDateAdded(String date){
        this.dateAdded = date;
    }

    /**
     * helper method that gets the current date
     * @return the current date in a string
     * */
    //@RequiresApi(api = Build.VERSION_CODES.N)
    protected static String currentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yy");
        return dateFormat.format(new Date());
    }

}
