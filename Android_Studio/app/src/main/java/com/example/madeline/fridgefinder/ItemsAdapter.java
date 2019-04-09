package com.example.madeline.fridgefinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * custom ArrayAdapteer that sets the display of
 * each list entry based on the FoodEntry
 * being referenced
 *
 * */

public class ItemsAdapter extends ArrayAdapter<FoodEntry> {

    /**
     * constructor that creates a new ItemsAdapter
     * @param context the current context
     * @param foods the list of foods to display
     * */

    public ItemsAdapter(Context context, ArrayList<FoodEntry> foods){
        super(context, 0 , foods);
    }
    /**
     * updates the view with the information
     * from the arraylist the adapter
     * was initialized with
     * */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FoodEntry food = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_food, parent, false);
        }
        // Lookup view for data population
        TextView dateAdded = (TextView) convertView.findViewById(R.id.date_added);
        TextView expDate = (TextView) convertView.findViewById(R.id.exp_date);
        TextView foodName = (TextView) convertView.findViewById(R.id.food_name);

        // Populate the data into the template view using the data object
        dateAdded.setText(food.dateAdded);
        expDate.setText("Exp date: " +food.dateExpiry);
        foodName.setText(food.foodName);
        // Return the completed view to render on screen
        return convertView;
    }
}